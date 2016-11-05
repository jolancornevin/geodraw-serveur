package spring.communication.message;

public class JoinGame extends Message {
    private final Long playerID;
    private final Long gameID;
    private final boolean observer;

    public JoinGame(Long playerID, Long gameID, boolean observer) {
        super(Type.JOINGAME);

        this.playerID = playerID;
        this.gameID = gameID;
        this.observer = observer;
    }

    public JoinGame(Long playerID, Long gameID) {
        super(Type.JOINGAME);

        this.playerID = playerID;
        this.gameID = gameID;
        this.observer = false;
    }

    public Long getPlayerID() {
        return playerID;
    }

    public Long getGameID() {
        return gameID;
    }

    public boolean isObserver() {
        return observer;
    }
}
