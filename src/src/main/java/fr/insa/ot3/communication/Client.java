package src.main.java.fr.insa.ot3.communication;

import java.io.IOException;

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
		
		try {
			socket = new SafeSocket(ip, port, HEART_BEAT_RATE, TIMEOUT, mess, breakdown);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		breakdown.add(new ClientBreak());
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
	
}
