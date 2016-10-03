package main.java.fr.insa.ot3.communication.message;

import main.java.fr.insa.ot3.model.Drawing;

public class TraceMessage extends Message
{
	
	private final Drawing trace;
	private final int gameID;
	private final String playerID;

	public TraceMessage(Drawing trace, int gameID, String playerID)
	{
		super(Type.TRACE);
		this.trace = trace;
		this.gameID = gameID;
		this.playerID = playerID;
	}


	public Drawing getTrace() {
		return trace;
	}


	public int getGameID() {
		return gameID;
	}


	public String getPlayerID() {
		return playerID;
	}
	
	
}
