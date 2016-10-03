package main.java.fr.insa.ot3.communication.message;

import main.java.fr.insa.ot3.model.Game;

public class GameUpdate extends Message
{
	private final Game updatedGame;

	public GameUpdate(Game game)
	{
		super(Type.GAMEUPDATE);
		updatedGame = game;
	}

	public Game getUpdatedGame() {
		return updatedGame;
	}
	
}
