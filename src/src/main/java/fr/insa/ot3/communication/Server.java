package src.main.java.fr.insa.ot3.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import src.main.java.fr.insa.ot3.communication.message.Message;
import src.main.java.fr.insa.ot3.utils.Utils;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.SafeSocket;

public class Server extends Side
{
	private List<SafeSocket> sockets;
	private ServerSocket servSock;
	
	public Server()
	{
		super();
		try {
			servSock = new ServerSocket(8080);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		breakdown.add(new ServerBreak());
	}
	
	public void startServer()
	{
		
		while(true)
		{
			try {
				SafeSocket s = new SafeSocket(servSock, HEART_BEAT_RATE, TIMEOUT, mess, breakdown);
				sockets.add(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
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
}
