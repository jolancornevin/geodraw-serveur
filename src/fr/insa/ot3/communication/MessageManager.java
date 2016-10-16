package fr.insa.ot3.communication;

import com.m5c.safesockets.MessageObserver;
import com.m5c.safesockets.SafeSocket;
import fr.insa.ot3.communication.message.*;

public class MessageManager implements MessageObserver {
    private Side side;

    public MessageManager(Side s) {
        side = s;
    }

    @Override
    public void notifyMessageObserver(SafeSocket sock, String message) {
        Message m = Message.parseMessage(message);

        //TODO remplacer par un switch
        if (m instanceof TraceMessage)
            side.HandleTraceMessage((TraceMessage) m, sock);

        else if (m instanceof GameList)
            side.HandleGameList((GameList) m, sock);

        else if (m instanceof GameListRequest)
            side.HandleGameListRequest((GameListRequest) m, sock);

        else if (m instanceof JoinGame)
            side.HandleJoinGame((JoinGame) m, sock);

        else if (m instanceof NewGame)
            side.HandleNewGame((NewGame) m, sock);

        else if (m instanceof AddLatLng)
            side.HandleAddLatLng((AddLatLng) m, sock);

        else if (m instanceof GameUpdate)
            side.HandleGameUpdate((GameUpdate) m, sock);

        else if (m instanceof JoinedGame)
            side.HandleJoinedGame((JoinedGame) m, sock);

        else if (m instanceof Vote)
            side.HandleVote((Vote) m, sock);
        else {
            //if there was an error
        }
    }

}
