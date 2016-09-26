package src.main.java.fr.insa.ot3.communication;

import java.util.Collection;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.MessageObserver;

public class Side 
{
	protected Collection<MessageObserver> mess;
	protected Collection<BreakdownObserver> breakdown;
	
	protected static int HEART_BEAT_RATE = 2000;
	protected static int TIMEOUT = 500;
	
	public Side()
	{
		mess.add(new MessageManager());
	}
	
}