package src.main.java.fr.insa.ot3.communication;

import java.io.IOException;

import src.main.java.fr.insa.ot3.communication.message.GameList;
import src.main.java.fr.insa.ot3.communication.message.GameListRequest;
import src.main.java.fr.insa.ot3.communication.message.JoinGame;
import src.main.java.fr.insa.ot3.communication.message.Message;
import src.main.java.fr.insa.ot3.communication.message.NewGame;
import src.main.java.fr.insa.ot3.communication.message.TraceMessage;
import src.main.java.fr.insa.ot3.utils.Utils;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.SafeSocket;

public class Client extends Side
{
	private SafeSocket socket;
	private final int port;
	private final String ip;
	
	public Client(String ip, int port)
	{
		super();
		this.port = port;
		this.ip = ip;

		breakdown.add(new ClientBreak());
		
		try {
			socket = new SafeSocket(ip, port, HEART_BEAT_RATE, TIMEOUT, mess, breakdown);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	

	
	public boolean sendMessage(Message m)
	{
		String jsonstr = Utils.gson.toJson(m);
		return socket.sendMessage(jsonstr);
	}
	
	public void disconnect()
	{
		socket.disconnect();
	}
	
	private void reconnect()
	{
		try {
			socket = new SafeSocket(ip, port, HEART_BEAT_RATE, TIMEOUT, mess, breakdown);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class ClientBreak implements BreakdownObserver
	{

		@Override
		public void notifyBreakdownObserver(SafeSocket arg0, boolean intended) {
			if(!intended)
				reconnect();
		}
		
	}

	@Override
	void HandleTraceMessage(TraceMessage m) {
		// TODO Auto-generated method stub
		
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
