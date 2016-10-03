package main.java.fr.insa.ot3.communication;

import java.util.Collection;
import java.util.LinkedList;

import main.java.fr.insa.ot3.communication.message.AddLatLng;
import main.java.fr.insa.ot3.communication.message.GameList;
import main.java.fr.insa.ot3.communication.message.GameListRequest;
import main.java.fr.insa.ot3.communication.message.GameUpdate;
import main.java.fr.insa.ot3.communication.message.JoinGame;
import main.java.fr.insa.ot3.communication.message.JoinedGame;
import main.java.fr.insa.ot3.communication.message.NewGame;
import main.java.fr.insa.ot3.communication.message.TraceMessage;
import main.java.fr.insa.ot3.communication.message.Vote;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.MessageObserver;
import com.m5c.safesockets.SafeSocket;


public abstract class Side 
{
	protected transient Collection<MessageObserver> mess;
	protected transient Collection<BreakdownObserver> breakdown;
	
	protected transient static int HEART_BEAT_RATE = 2000;
	protected transient static int TIMEOUT = 500;
	
	public Side()
	{
		mess = new LinkedList<MessageObserver>();
		breakdown = new LinkedList<BreakdownObserver>();
		mess.add(new MessageManager(this));
	}
	
	abstract void HandleTraceMessage(TraceMessage m, SafeSocket sender);
	
	abstract void HandleAddLatLng(AddLatLng m, SafeSocket sender);

	abstract void HandleGameList(GameList m, SafeSocket sender);

	abstract void HandleGameUpdate(GameUpdate m, SafeSocket sender);

	abstract void HandleGameListRequest(GameListRequest m, SafeSocket sender);

	abstract void HandleJoinGame(JoinGame m, SafeSocket sender);

	abstract void HandleJoinedGame(JoinedGame m, SafeSocket sender);
	
	abstract void HandleNewGame(NewGame m, SafeSocket sender);
	
	abstract void HandleVote(Vote m, SafeSocket sender);
	
}
