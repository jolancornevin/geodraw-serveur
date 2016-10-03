package fr.insa.ot3.communication.message;

public class Vote extends Message{

	private final int gameID;
	private final String voter;
	private final String electedPlayer;
	
	public Vote(int gameID, String voter, String electedPlayer)
	{
		super(Type.VOTE);
		
		this.gameID = gameID;
		this.voter = voter;
		this.electedPlayer = electedPlayer;
	}

	public int getGameID() {
		return gameID;
	}

	public String getVoter() {
		return voter;
	}

	public String getElectedPlayer() {
		return electedPlayer;
	}
}
