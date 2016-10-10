package fr.insa.ot3.communication.message;

import java.util.Collection;

import fr.insa.ot3.model.Game;

public class GameList extends Message {

	private final List<GameInfo> games;
	

	public GameList(List<GameInfo> games) 
	{
		super(Type.GAMELIST);
		this.games = games;
	}

	public List<GameInfo> getGames() {
		return games;
	}
}
