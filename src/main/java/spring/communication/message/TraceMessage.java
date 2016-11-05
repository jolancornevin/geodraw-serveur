package spring.communication.message;

import spring.models.Drawing;

public class TraceMessage extends Message {

    private final Drawing trace;
    private final Long gameID;
    private final Long playerID;

    public TraceMessage(Drawing trace, Long gameID, Long playerID) {
        super(Type.TRACE);
        this.trace = trace;
        this.gameID = gameID;
        this.playerID = playerID;
    }


    public Drawing getTrace() {
        return trace;
    }


    public Long getGameID() {
        return gameID;
    }


    public Long getPlayerID() {
        return playerID;
    }


}
