package spring.communication.message;

public class GameListRequest extends Message {

    private final String userID;
    private final boolean self;

    public GameListRequest() {
        super(Type.GAMELISTREQUEST);
        userID = "";
        self = false;
    }


    public GameListRequest(String userID) {
        super(Type.GAMELISTREQUEST);
        this.userID = userID;
        self = true;
    }

    public boolean isSelf() {
        return self;
    }

    public String getUserID() {
        return userID;
    }
}
