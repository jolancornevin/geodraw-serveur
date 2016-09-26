package src.main.java.fr.insa.ot3.communication;

import src.main.java.fr.insa.ot3.communication.message.GameList;
import src.main.java.fr.insa.ot3.communication.message.GameListRequest;
import src.main.java.fr.insa.ot3.communication.message.JoinGame;
import src.main.java.fr.insa.ot3.communication.message.Message;
import src.main.java.fr.insa.ot3.communication.message.NewGame;
import src.main.java.fr.insa.ot3.communication.message.TraceMessage;

import com.m5c.safesockets.MessageObserver;
import com.m5c.safesockets.SafeSocket;

public class MessageManager implements MessageObserver
{
	private Side side;
	
	public MessageManager(Side s) 
	{
		side = s;
	}
	
	@Override
	public void notifyMessageObserver(SafeSocket sock, String message) 
	{
		Message m = Message.parseMessage(message);
		
		if(m instanceof TraceMessage)
			side.HandleTraceMessage((TraceMessage) m);
		else if(m instanceof GameList)
			side.HandleGameList((GameList) m);
		else if(m instanceof GameListRequest)
			side.HandleGameListRequest((GameListRequest) m);
		else if(m instanceof JoinGame)
			side.HandleJoinGame((JoinGame) m);
		else if(m instanceof NewGame)
			side.HandleNewGame((NewGame) m);
		else
		{
			//if there was an error
		}
	}

}
