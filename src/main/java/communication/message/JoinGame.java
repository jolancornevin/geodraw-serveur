package communication.message;

public class JoinGame extends Message {
    private final String playerID;
    private final int gameID;
    private final boolean observer;


    public JoinGame(String playerID, int gameID, boolean observer) {
        super(Type.JOINGAME);

        this.playerID = playerID;
        this.gameID = gameID;
        this.observer = observer;
    }

    public JoinGame(String playerID, int gameID) {
        super(Type.JOINGAME);

        this.playerID = playerID;
        this.gameID = gameID;
        this.observer = false;
    }

    public String getPlayerID() {
        return playerID;
    }


    public int getGameID() {
        return gameID;
    }


    public boolean isObserver() {
        return observer;
    }

}
