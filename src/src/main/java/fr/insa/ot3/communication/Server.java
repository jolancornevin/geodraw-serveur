package src.main.java.fr.insa.ot3.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import src.main.java.fr.insa.ot3.communication.message.GameList;
import src.main.java.fr.insa.ot3.communication.message.GameListRequest;
import src.main.java.fr.insa.ot3.communication.message.JoinGame;
import src.main.java.fr.insa.ot3.communication.message.JoinedGame;
import src.main.java.fr.insa.ot3.communication.message.Message;
import src.main.java.fr.insa.ot3.communication.message.NewGame;
import src.main.java.fr.insa.ot3.communication.message.TraceMessage;
import src.main.java.fr.insa.ot3.model.Game;
import src.main.java.fr.insa.ot3.utils.Utils;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.SafeSocket;

public class Server extends Side
{
	private transient List<SafeSocket> sockets;
	private transient ServerSocket servSock;

	private transient Thread socketCreator;

	private final Map<Integer, Game> gameList;
	private final transient Map<Integer, List<SafeSocket>> gameSubscribers;
	
	private int id = 0;

	public static void main(String[] args)
	{
		Server s;
		
//		try{
//			String serverSave = Utils.readFile("./server.json", Charset.defaultCharset());
//			s = Utils.gson.fromJson(serverSave, Server.class);
//		} catch(Exception e)
//		{
			s = new Server();
//		}
		
		s.start();
		
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		
		s.stop();
		
		String serverSave = Utils.gson.toJson(s);
		
		PrintWriter writer;
		try {
			writer = new PrintWriter("./server.json", "UTF-8");
			writer.print(serverSave);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	public Server()
	{
		super();

		gameList = new HashMap<Integer, Game>();
		gameSubscribers = new HashMap<Integer, List<SafeSocket>>();

		sockets = new LinkedList<SafeSocket>();
		breakdown.add(new ServerBreak());

		try {
			servSock = new ServerSocket(8080);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Collection<Game> getGameList() {
		return gameList.values();
	}
	
	private transient boolean interrupted = false;

	public void start()
	{
		socketCreator = new Thread("Server Socket creator"){
			public void run() {
				while(!interrupted)
				{
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
	}

	public void stop()
	{
		try {
			interrupted = true;
			Client c = new Client("localhost", 8080);
			Thread.sleep(500);
			c.disconnect();
			servSock.close();
			socketCreator.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.err.println("Server stopped");
	}

	public void sendMessageToAll(Message m)
	{
		String jsonstr = Utils.gson.toJson(m);
		for(SafeSocket s : sockets)
			s.sendMessage(jsonstr);
	}

	public void sendMessageTo(SafeSocket s, Message m)
	{
		String jsonstr = Utils.gson.toJson(m);
		s.sendMessage(jsonstr);
	}

	public void sendMessageToGame(int gameID, Message m)
	{
		List<SafeSocket> ls = gameSubscribers.get(gameID);
		if(ls == null)
			return;
		
		List<SafeSocket> dead = new LinkedList<SafeSocket>();
		
		for(SafeSocket s : ls)
		{
			if(s.isSocketAlive())
				sendMessageTo(s, m);
			else
				dead.add(s);
		}
		
		for(SafeSocket s : dead)
			ls.remove(s);
		
	}
	
	class ServerBreak implements BreakdownObserver
	{
		@Override
		public void notifyBreakdownObserver(SafeSocket sock, boolean intended) 
		{
			sockets.remove(sock);
		}

	}

	@Override
	void HandleTraceMessage(TraceMessage m, SafeSocket sender) {
		this.sendMessageToGame(m.getGameID(), m);
		
		if(gameList.containsKey(m.getGameID()))
			gameList.get(m.getGameID()).updateTrace(m.getPlayerID(), m.getTrace());
	}

	/** Client method */
	@Override
	void HandleGameList(GameList m, SafeSocket sender) {return;}

	@Override
	void HandleGameListRequest(GameListRequest m, SafeSocket sender) 
	{
		sendMessageTo(sender, new GameList(getGameList()));
	}

	@Override
	void HandleJoinGame(JoinGame m, SafeSocket sender) {
		if(!gameList.containsKey(m.getGameID()))
			sendMessageTo(sender, new JoinedGame(false));
		Game g = gameList.get(m.getGameID());

		subscribeToGame(sender, m.getGameID());
		
		if(m.isObserver())
			sendMessageTo(sender, new JoinedGame(true));
		else
			sendMessageTo(sender, new JoinedGame(g.addPlayer(m.getPlayerID())));
	}

	/** Client method */
	@Override
	void HandleJoinedGame(JoinedGame m, SafeSocket sender) {return;}

	
	@Override
	void HandleNewGame(NewGame m, SafeSocket sender) {
		int gID = id;
		id++;
		Game g = new Game(gID, m.getName(), m.isLock(), 0, m.getMaxNbPlayer(), m.getStartDate(), m.getEndDate(), m.getTheme());
		g.addPlayer(m.getPlayerID());
		gameList.put(gID, g);
		subscribeToGame(sender, gID);
	}
	
	private void subscribeToGame(SafeSocket s, int gameID)
	{
		if(!gameSubscribers.containsKey(gameID))
		{
			List<SafeSocket> ls = new LinkedList<SafeSocket>();
			ls.add(s);
			gameSubscribers.put(gameID, ls);
		}
		else
		{
			gameSubscribers.get(gameID).add(s);
		}
	}

}
