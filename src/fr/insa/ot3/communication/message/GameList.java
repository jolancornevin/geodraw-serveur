package fr.insa.ot3.communication.message;

import java.util.Collection;

import fr.insa.ot3.model.Game;

public class GameList extends Message {

	private final List<GameInfo> games;
	
	/* true if this is a personnal game list */
	private final boolean self;
	

	public GameList(List<GameInfo> games, boolean self) 
	{
		super(Type.GAMELIST);
		this.games = games;
		this.self = self;
	}

	public List<GameInfo> getGames() {
		return games;
	}
	
	public boolean isSelf()
	{
		return self;
	}
}
