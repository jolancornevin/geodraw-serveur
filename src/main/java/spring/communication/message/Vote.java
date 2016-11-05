package spring.communication.message;

public class Vote extends Message {

    private final Long gameID;
    private final long voter;
    private final long electedPlayer;

    public Vote(Long gameID, long voter, long electedPlayer) {
        super(Type.VOTE);

        this.gameID = gameID;
        this.voter = voter;
        this.electedPlayer = electedPlayer;
    }

    public Long getGameID() {
        return gameID;
    }

    public long getVoter() {
        return voter;
    }

    public long getElectedPlayer() {
        return electedPlayer;
    }
}
