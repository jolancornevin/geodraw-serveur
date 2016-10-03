package src.main.java.fr.insa.ot3.communication.message;

import java.util.Collection;

import src.main.java.fr.insa.ot3.model.Game;

public class GameList extends Message {

	private final Collection<Game> games;
	

	public GameList(Collection<Game> games) 
	{
		super(Type.GAMELIST);
		this.games = games;
	}

	public Collection<Game> getGames() {
		return games;
	}
}
