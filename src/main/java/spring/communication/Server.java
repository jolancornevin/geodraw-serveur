package spring.communication;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.SafeSocket;
import javassist.tools.web.BadHttpRequest;
import spring.communication.message.*;
import spring.models.Game;
import spring.models.GameInfo;
import spring.utils.Connexion;
import spring.utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class Server extends Side {

    /* =================================================================================================================
                                                        Variables
    ================================================================================================================= */
    private transient List<SafeSocket> sockets;
    private transient ServerSocket servSock;

    private transient Thread socketCreator;
    private transient Thread gameCleaner;

    private final Map<Long, Game> currentGamesList;
    private final List<Game> finishedGames;
    private final transient Map<Long, List<SafeSocket>> gameSubscribers;

    private transient boolean interrupted = false;
    private Long id = 0L;

    /* =================================================================================================================
                                                        Constructeur
    ================================================================================================================= */

    public Server() {
        super();

        currentGamesList = new HashMap<>();
        finishedGames = new LinkedList<>();
        gameSubscribers = new HashMap<>();

        sockets = new LinkedList<>();
        breakdown.add(new ServerBreak());

        try {
            servSock = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /* =================================================================================================================
                                                  Init serveur function
    ================================================================================================================= */

    public void start() {
        for (Game game : Connexion.getInstance().getGameController().getAllWithPlayers()) {
            currentGamesList.put(game.getId(), game);
        }

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

        //Job tournant toutes les secondes, pour voir si des game on finis.
        gameCleaner = new Thread("Server finished games cleaner") {
            public void run() {
                int preIndex, postIndex;
                while (!interrupted) {
                    Date now = new Date();

                    //TODO : save traces and game in DB --> add method in controller
                    for (Game g : currentGamesList.values()) {
                        if (!g.isOver() && now.after(g.getEndDate())) {
                            g.setOver(true);

                            finishedGames.add(g);
                            currentGamesList.remove(g.getId());

                            //Save it to the database
                            Connexion.getInstance().getGameController().save(g);
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        };

        socketCreator.start();
        gameCleaner.start();
    }

    public void stop() {
        try {
            interrupted = true;
            gameCleaner.interrupt();
            //Client c = new Client("localhost", 8080);
            Thread.sleep(500);
            //c.disconnect();
            servSock.close();
            socketCreator.join();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        //TODO : save live elements in database

        System.err.println("Server stopped");
    }

    /* =================================================================================================================
                                                  Socket functions
    ================================================================================================================= */
    class ServerBreak implements BreakdownObserver {
        @Override
        public void notifyBreakdownObserver(SafeSocket sock, boolean intended) {
            sockets.remove(sock);
            for (List<SafeSocket> l : gameSubscribers.values())
                l.remove(sock);
        }
    }

    /**
     * Get the list of game in progress. Those games can be specific for a player or not (depending on the request)
     * //TODO move to REST API
     *
     * @param m
     * @param sender
     */
    @Override
    void HandleGameListRequest(GameListRequest m, SafeSocket sender) {
        if (m.isSelf())
            sendMessageTo(sender, new GameList(getGameListFor(m.getUserID()), true));
        else
            sendMessageTo(sender, new GameList(getCurrentGamesList(), false));
    }

    /**
     * Put the gamer inside a game
     * //TODO move to REST API
     *
     * @param m
     * @param sender
     */
    @Override
    void HandleJoinGame(JoinGame m, SafeSocket sender) {
        if (!currentGamesList.containsKey(m.getGameID()))
            sendMessageTo(sender, new JoinedGame(false));

        subscribeToGame(sender, m.getGameID());

        if (m.isObserver()) {
            //TODO à remplacer pour que ça fonctionne
            sendMessageTo(sender, new JoinedGame(true));
        } else {
            boolean joined = joinGameInBd(m.getGameID(), m.getPlayerID());
            sendMessageTo(sender, new JoinedGame(joined));

            if (joined) {
                /*
                Segment segment1 = new Segment();
                segment1.addLatLng(new LatLng(10, 10));
                segment1.addLatLng(new LatLng(10, 11));
                segment1.addLatLng(new LatLng(12, 14));
                segment1.addLatLng(new LatLng(15, 2));

                Segment segment2 = new Segment();
                segment2.addLatLng(new LatLng(24, 13));
                segment2.addLatLng(new LatLng(24, 15));
                segment2.addLatLng(new LatLng(26, 8));
                segment2.addLatLng(new LatLng(28, 4));

                Segment segment3 = new Segment();
                segment3.addLatLng(new LatLng(32, 12));
                segment3.addLatLng(new LatLng(32, 13));
                segment3.addLatLng(new LatLng(34, 16));
                segment3.addLatLng(new LatLng(32, 4));

                Segment segment4 = new Segment();
                segment4.addLatLng(new LatLng(46, 15));
                segment4.addLatLng(new LatLng(46, 17));
                segment4.addLatLng(new LatLng(48, 10));
                segment4.addLatLng(new LatLng(40, 6));

                Drawing g = new Drawing();
                g.addSegment(segment1);
                g.addSegment(segment2);

                Drawing h = new Drawing();
                h.addSegment(segment3);
                h.addSegment(segment4);

                sendMessageTo(sender, new TraceMessage(g, m.getGameID(), "adrn"));
                sendMessageTo(sender, new TraceMessage(h, m.getGameID(), "adrn"));*/
            }
        }
    }

    /**
     * Create new game in the database and put the creator inside
     * //TODO move to REST API
     *
     * @param m
     * @param sender
     */
    @Override
    void HandleNewGame(NewGame m, SafeSocket sender) {
        Game g = new Game(m.getName(), m.isLock(), m.getMaxNbPlayer(), m.getHours(), m.getMins(), m.getTheme(), false);

        //Create the game in the database
        try {
            Connexion.getInstance().getGameController().create(g);
        } catch (BadHttpRequest badHttpRequest) {
            badHttpRequest.printStackTrace();
            //TODO return error
        }

        //Join the game
        if (joinGameInBd(g.getId(), m.getPlayerID())) {
            currentGamesList.put(g.getId(), g);
            subscribeToGame(sender, g.getId());
        } else {
            //TODO return error
        }
    }

    /**
     * Ajout d'un latlng dans le game en cours par le client.
     *
     * @param m
     * @param sender
     */
    @Override
    void HandleAddLatLng(AddLatLng m, SafeSocket sender) {
        //TODO gestion de isDrawing or not
        //TODO gestion player is in game and both exist

        //Broadcast the message to all players
        sendMessageToGame(m.getGameID(), m);

        Game game = currentGamesList.get(m.getGameID());

        //Add the latlng to the java objet
        game.getTrace(m.getUserID()).addLatLng(m.getLatLng(), m.isDrawing());

        //Save it to the database
        Connexion.getInstance().getGameController().save(game);
    }

    @Override
    void HandleVote(Vote m, SafeSocket sender) {
        currentGamesList.get(m.getGameID()).voteFor(m.getElectedPlayer());
    }

    /* =================================================================================================================
                                                  Socket CLIENT functions
    ================================================================================================================= */
    @Override
    void HandleTraceMessage(TraceMessage m, SafeSocket sender) {

    }

    @Override
    void HandleGameList(GameList m, SafeSocket sender) {
        return;
    }

    @Override
    void HandleJoinedGame(JoinedGame m, SafeSocket sender) {
        return;
    }

    @Override
    void HandleGameUpdate(GameUpdate m, SafeSocket sender) {
        // Auto-generated method stub
    }

    /* =================================================================================================================
                                                  Functions
    ================================================================================================================= */

    /**
     * Récupère la liste complète des games
     *
     * @return
     */
    private List<GameInfo> getCurrentGamesList() {
        List<GameInfo> lst = new LinkedList<>();
        for (Game g : currentGamesList.values()) {
            lst.add(new GameInfo(g));
        }
        return lst;
    }

    /**
     * Récupère la liste complète des games pour un joueur donnée
     *
     * @return
     */
    private List<GameInfo> getGameListFor(Long userID) {
        List<GameInfo> lst = new LinkedList<>();

        //TODO à remplacer par une requête sur la bd, pour éviter de tous parcourir
        for (Game g : currentGamesList.values()) {
            if (g.hasPlayer(userID))
                lst.add(new GameInfo(g));
        }
        return lst;
    }

    private void sendMessageToAll(Message m) {
        String jsonstr = Utils.gson.toJson(m);
        for (SafeSocket s : sockets)
            s.sendMessage(jsonstr);
    }

    private void sendMessageTo(SafeSocket s, Message m) {
        String jsonstr = Utils.gson.toJson(m);
        s.sendMessage(jsonstr);
    }

    private void sendMessageToGame(Long gameID, Message m) {
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

    private boolean joinGameInBd(Long gameId, Long playerId) {
        try {
            Connexion.getInstance().getGameController().joinGame(gameId, playerId);
            return true;
        } catch (BadHttpRequest badHttpRequest) {
            badHttpRequest.printStackTrace();
        }
        return false;
    }

    private void subscribeToGame(SafeSocket s, Long gameID) {
        if (!gameSubscribers.containsKey(gameID)) {
            List<SafeSocket> ls = new LinkedList<SafeSocket>();
            ls.add(s);
            gameSubscribers.put(gameID, ls);
        } else {
            gameSubscribers.get(gameID).add(s);
        }
    }

}