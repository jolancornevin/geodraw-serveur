package communication.message;


public class JoinedGame extends Message {

    private final boolean joined;

    public JoinedGame(boolean joined) {
        super(Type.JOINEDGAME);
        this.joined = joined;
    }

    public boolean isJoined() {
        return joined;
    }
}
