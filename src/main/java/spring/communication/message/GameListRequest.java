package spring.communication.message;

public class GameListRequest extends Message {

    private final Long userID;
    private final boolean self;

    public GameListRequest() {
        super(Type.GAMELISTREQUEST);
        userID = -1L;
        self = false;
    }

    public GameListRequest(Long userID) {
        super(Type.GAMELISTREQUEST);
        this.userID = userID;
        self = true;
    }

    public boolean isSelf() {
        return self;
    }

    public Long getUserID() {
        return userID;
    }
}
