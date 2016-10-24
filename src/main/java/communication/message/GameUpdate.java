package communication.message;

import model.Game;

public class GameUpdate extends Message {
    private final Game updatedGame;

    public GameUpdate(Game game) {
        super(Type.GAMEUPDATE);
        updatedGame = game;
    }

    public Game getUpdatedGame() {
        return updatedGame;
    }

}
