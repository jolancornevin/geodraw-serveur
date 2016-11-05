package spring.communication.message;

public class GameListRequest extends Message {

    private final long userID;
    private final boolean self;

    public GameListRequest() {
        super(Type.GAMELISTREQUEST);
        userID = 0;
        self = false;
    }


    public GameListRequest(long userID) {
        super(Type.GAMELISTREQUEST);
        this.userID = userID;
        self = true;
    }

    public boolean isSelf() {
        return self;
    }

    public long getUserID() {
        return userID;
    }
}
