package src.main.java.fr.insa.ot3.communication;

import src.main.java.fr.insa.ot3.communication.message.Message;
import src.main.java.fr.insa.ot3.communication.message.TraceMessage;

import com.m5c.safesockets.MessageObserver;
import com.m5c.safesockets.SafeSocket;

public class MessageManager implements MessageObserver
{

	@Override
	public void notifyMessageObserver(SafeSocket sock, String message) 
	{
		Message m = Message.parseMessage(message);
		
		if(m instanceof TraceMessage)
		{
			//TODO things
		}
		else
		{
			//if there was an error
		}
	}

}
