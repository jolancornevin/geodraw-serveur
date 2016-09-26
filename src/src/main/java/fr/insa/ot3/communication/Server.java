package src.main.java.fr.insa.ot3.communication;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.MessageObserver;
import com.m5c.safesockets.SafeSocket;

public class Server 
{
	private List<SafeSocket> sockets;
	
	private Collection<MessageObserver> mess;
	private Collection<BreakdownObserver> breakdown;
	
	private static int HEART_BEAT_RATE = 2000;
	private static int TIMEOUT = 500;
	
	public Server()
	{
		sockets = new LinkedList<SafeSocket>();
		
		mess.add(new MessageManager());
		breakdown.add(new ServerBreak());
	}
	
	public void startServer()
	{
		
		while(true)
		{
			try {
				SafeSocket s = new SafeSocket(8080, HEART_BEAT_RATE, TIMEOUT, mess, breakdown);
				sockets.add(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
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
