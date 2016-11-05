package spring.communication;


import com.m5c.safesockets.SafeSocket;
import spring.communication.message.*;

/**
 * Created by arda on 05/10/16.
 */

public abstract class ClientListener {
    /*
    private Handler mHandler;
    public ClientListener(){
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                //Depending on the type of message, call a method
            }
        };
    } */
    void HandleTraceMessage(TraceMessage m, SafeSocket sender) {

    }

    void HandleGameList(GameList m, SafeSocket sender) {
        // TODO Auto-generated method stub

    }


    void HandleJoinedGame(JoinedGame m, SafeSocket sender) {
        // TODO Auto-generated method stub

    }


    void HandleAddLatLng(AddLatLng m, SafeSocket sender) {
        // TODO Auto-generated method stub

    }


    void HandleGameUpdate(GameUpdate m, SafeSocket sender) {
        // TODO Auto-generated method stub

    }

    void HandleVote(Vote m, SafeSocket sender) {
        // TODO Auto-generated method stub

    }

    void HandleNewUser(NewUser m, SafeSocket sender) {

    }


}