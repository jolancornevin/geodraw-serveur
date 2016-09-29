package src.main.java.fr.insa.ot3.communication;

import java.util.Collection;
import java.util.LinkedList;

import src.main.java.fr.insa.ot3.communication.message.GameList;
import src.main.java.fr.insa.ot3.communication.message.GameListRequest;
import src.main.java.fr.insa.ot3.communication.message.JoinGame;
import src.main.java.fr.insa.ot3.communication.message.NewGame;
import src.main.java.fr.insa.ot3.communication.message.TraceMessage;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.MessageObserver;


public abstract class Side 
{
	protected Collection<MessageObserver> mess;
	protected Collection<BreakdownObserver> breakdown;
	
	protected static int HEART_BEAT_RATE = 2000;
	protected static int TIMEOUT = 500;
	
	public Side()
	{
		mess = new LinkedList<MessageObserver>();
		breakdown = new LinkedList<BreakdownObserver>();
		mess.add(new MessageManager(this));
	}
	
	abstract void HandleTraceMessage(TraceMessage m);

	abstract void HandleGameList(GameList m);

	abstract void HandleGameListRequest(GameListRequest m);

	abstract void HandleJoinGame(JoinGame m);

	abstract void HandleNewGame(NewGame m);
	
}
