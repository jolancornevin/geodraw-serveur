package fr.insa.ot3.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.SafeSocket;

import fr.insa.ot3.communication.message.AddLatLng;
import fr.insa.ot3.communication.message.GameList;
import fr.insa.ot3.communication.message.GameListRequest;
import fr.insa.ot3.communication.message.GameUpdate;
import fr.insa.ot3.communication.message.JoinGame;
import fr.insa.ot3.communication.message.JoinedGame;
import fr.insa.ot3.communication.message.Message;
import fr.insa.ot3.communication.message.NewGame;
import fr.insa.ot3.communication.message.TraceMessage;
import fr.insa.ot3.communication.message.Vote;
import fr.insa.ot3.model.Game;
import fr.insa.ot3.model.GameInfo;
import fr.insa.ot3.model.LatLng;
import fr.insa.ot3.utils.Color;
import fr.insa.ot3.utils.Utils;

public class Server extends Side {
    private transient List<SafeSocket> sockets;
    private transient ServerSocket servSock;

    private transient Thread socketCreator;
    private transient Thread gameCleaner;

    private final Map<Integer, Game> gameList;
    private final List<Game> finishedGames;
    private final transient Map<Integer, List<SafeSocket>> gameSubscribers;

    private int id = 0;

    public Server(int port) {
        super();

        gameList = new HashMap<Integer, Game>();
        finishedGames = new LinkedList<Game>();
        gameSubscribers = new HashMap<Integer, List<SafeSocket>>();

        sockets = new LinkedList<SafeSocket>();
        breakdown.add(new ServerBreak());

        try {
            servSock = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Server() {
    	this(8080);
    }

    public List<GameInfo> getGameList() {
        List<GameInfo> lst = new LinkedList<>();
        for (Game g : gameList.values()) {
            lst.add(new GameInfo(g));
        }
        return lst;
    }


    public List<GameInfo> getGameListFor(String userID) {
        List<GameInfo> lst = new LinkedList<>();
        for (Game g : gameList.values()) {
            if (g.hasPlayer(userID))
                lst.add(new GameInfo(g));
        }
        return lst;
    }

    private transient boolean interrupted = false;

    public void start() {
        socketCreator = new Thread("Server Socket creator") {
            public void run() {
                while (!interrupted) {
                    try {
                        SafeSocket s = new SafeSocket(servSock, HEART_BEAT_RATE, TIMEOUT, mess, breakdown);

                        sockets.add(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        socketCreator.start();

        gameCleaner = new Thread("Server finished games cleaner") {
            public void run() {
                int preIndex, postIndex;
                while (!interrupted) {

                    preIndex = finishedGames.size();

                    Date now = new Date();
                    for (Game g : gameList.values()) {
                        if (now.after(g.getEndDate()))
                            finishedGames.add(g);
                    }


                    postIndex = finishedGames.size();
                    if (postIndex > preIndex) // if there is some new finished games
                    {
                        for (int i = preIndex; i < postIndex; i++) {
                            gameList.remove(finishedGames.get(i).getId());
                        }
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        };
        gameCleaner.start();

		System.out.println(Color.CYAN + " Server started!" + Color.RESET);
    }

    public void stop() {
        try {
            interrupted = true;
            gameCleaner.interrupt();
            servSock.close();
            socketCreator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Color.CYAN + "Server stopped" + Color.RESET);
    }

    public void sendMessageToAll(Message m) {
        String jsonstr = Utils.gson.toJson(m);
        for (SafeSocket s : sockets)
            s.sendMessage(jsonstr);
    }

    public void sendMessageTo(SafeSocket s, Message m) {
        String jsonstr = Utils.gson.toJson(m);
        s.sendMessage(jsonstr);
    }

    public void sendMessageToGame(int gameID, Message m) {
        List<SafeSocket> ls = gameSubscribers.get(gameID);
        if (ls == null)
            return;

        List<SafeSocket> dead = new LinkedList<SafeSocket>();

        for (SafeSocket s : ls) {
            if (s.isSocketAlive())
                sendMessageTo(s, m);
            else
                dead.add(s);
        }

        for (SafeSocket s : dead)
            ls.remove(s);

    }

    class ServerBreak implements BreakdownObserver {
        @Override
        public void notifyBreakdownObserver(SafeSocket sock, boolean intended) {
            sockets.remove(sock);
            for(List<SafeSocket> l : gameSubscribers.values())
            	l.remove(sock);
        }

    }

    @Override
    void HandleTraceMessage(TraceMessage m, SafeSocket sender) {
        this.sendMessageToGame(m.getGameID(), m);

        if (gameList.containsKey(m.getGameID()))
            gameList.get(m.getGameID()).updateTrace(m.getPlayerID(), m.getTrace());
    }

    /**
     * Client method
     */
    @Override
    void HandleGameList(GameList m, SafeSocket sender) {
        return;
    }

    @Override
    void HandleGameListRequest(GameListRequest m, SafeSocket sender) {
        if (m.isSelf())
            sendMessageTo(sender, new GameList(getGameListFor(m.getUserID()), true));
        else
            sendMessageTo(sender, new GameList(getGameList(), false));
    }

    @Override
    void HandleJoinGame(JoinGame m, SafeSocket sender) {
        if (!gameList.containsKey(m.getGameID()))
            sendMessageTo(sender, new JoinedGame(false));
        Game g = gameList.get(m.getGameID());


        boolean joined;
        
        if (m.isObserver()) {
        	joined = true;
            sendMessageTo(sender, new JoinedGame(joined));
        }
        else {
        	joined = g.addPlayer(m.getPlayerID());
            sendMessageTo(sender, new JoinedGame(joined));
        }
        
        if(joined) {
        	sendMessageToGame(m.getGameID(), new AddLatLng(m.getPlayerID(), m.getGameID(), new LatLng(0, 0), false));
        	sendMessageTo(sender, new GameUpdate(g));
        }

        subscribeToGame(sender, m.getGameID());
    }

    /**
     * Client method
     */
    @Override
    void HandleJoinedGame(JoinedGame m, SafeSocket sender) {
        return;
    }


    @Override
    void HandleNewGame(NewGame m, SafeSocket sender) {
        int gID = id++;

        Game g = new Game(gID, m.getName(), m.isLock(), 0, m.getMaxNbPlayer(), m.getHours(), m.getMins(), m.getTheme());
        g.addPlayer(m.getPlayerID());
        gameList.put(gID, g);

    	sendMessageTo(sender, new GameUpdate(g));
        subscribeToGame(sender, gID);
    }

    private void subscribeToGame(SafeSocket s, int gameID) {
        if (!gameSubscribers.containsKey(gameID)) {
            List<SafeSocket> ls = new LinkedList<SafeSocket>();
            ls.add(s);
            gameSubscribers.put(gameID, ls);
        } else {
            gameSubscribers.get(gameID).add(s);
        }
    }

    @Override
    void HandleAddLatLng(AddLatLng m, SafeSocket sender) {
        sendMessageToGame(m.getGameID(), m);

        gameList.get(m.getGameID()).getTrace(m.getUserID()).addLatLng(m.getLatLng(), m.isDrawing());
    }

    @Override
    void HandleGameUpdate(GameUpdate m, SafeSocket sender) {
        // TODO Auto-generated method stub
    }

    @Override
    void HandleVote(Vote m, SafeSocket sender) {
        gameList.get(m.getGameID()).voteFor(m.getElectedPlayer());
    }

}
