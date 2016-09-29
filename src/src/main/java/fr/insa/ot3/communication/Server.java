package src.main.java.fr.insa.ot3.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import src.main.java.fr.insa.ot3.communication.message.GameList;
import src.main.java.fr.insa.ot3.communication.message.GameListRequest;
import src.main.java.fr.insa.ot3.communication.message.JoinGame;
import src.main.java.fr.insa.ot3.communication.message.Message;
import src.main.java.fr.insa.ot3.communication.message.NewGame;
import src.main.java.fr.insa.ot3.communication.message.TraceMessage;
import src.main.java.fr.insa.ot3.utils.Utils;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.SafeSocket;

public class Server extends Side
{
	private List<SafeSocket> sockets;
	private ServerSocket servSock;

	private Thread socketCreator;

	public Server()
	{
		super();

		sockets = new LinkedList<SafeSocket>();
		breakdown.add(new ServerBreak());

		try {
			servSock = new ServerSocket(8080);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean interrupted = false;

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

	class ServerBreak implements BreakdownObserver
	{
		@Override
		public void notifyBreakdownObserver(SafeSocket sock, boolean intended) 
		{
			sockets.remove(sock);
		}

	}

	@Override
	void HandleTraceMessage(TraceMessage m) {
		System.out.println(Utils.gson.toJson(m.getTrace()));

		this.sendMessageToAll(m);
	}

	@Override
	void HandleGameList(GameList m) {
		// TODO Auto-generated method stub

	}

	@Override
	void HandleGameListRequest(GameListRequest m) {
		// TODO Auto-generated method stub

	}

	@Override
	void HandleJoinGame(JoinGame m) {
		// TODO Auto-generated method stub

	}

	@Override
	void HandleNewGame(NewGame m) {
		// TODO Auto-generated method stub

	}
}
