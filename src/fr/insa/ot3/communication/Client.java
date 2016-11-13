package fr.insa.ot3.communication;

import com.m5c.safesockets.BreakdownObserver;
import com.m5c.safesockets.SafeSocket;

import fr.insa.ot3.communication.message.*;
import fr.insa.ot3.model.GameInfo;
import fr.insa.ot3.utils.Utils;

import java.io.IOException;

public class Client extends Side {
    private SafeSocket socket;
    private final int port;
    private final String ip;

    public Client(String ip, int port) {
        super();
        this.port = port;
        this.ip = ip;

        breakdown.add(new ClientBreak());

        try {
            socket = new SafeSocket(ip, port, HEART_BEAT_RATE, TIMEOUT, mess, breakdown);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(Message m) {
        String jsonstr = Utils.gson.toJson(m);
        return socket.sendMessage(jsonstr);
    }

    public void disconnect() {
    	if(socket != null && socket.isSocketAlive())
    		socket.disconnect();
    }

    private void reconnect() {
        try {
            socket = new SafeSocket(ip, port, HEART_BEAT_RATE, TIMEOUT, mess, breakdown);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientBreak implements BreakdownObserver {

        @Override
        public void notifyBreakdownObserver(SafeSocket arg0, boolean intended) {
            if (!intended)
                reconnect();
        }

    }

    @Override
    void HandleTraceMessage(TraceMessage m, SafeSocket sender) {
//        System.out.println(Utils.gson.toJson(m.getTrace()));

    }


    @Override
    void HandleGameList(GameList m, SafeSocket sender) {
    	System.out.println("Liste des parties:");

    	System.out.println("\t" + GameInfo.header());
    	
    	for(GameInfo g : m.getGames())
    		System.out.println("\t" + g);
    }


    /**
     * Server method
     */
    @Override
    void HandleGameListRequest(GameListRequest m, SafeSocket sender) {
        return;
    }


    /**
     * Server method
     */
    @Override
    void HandleJoinGame(JoinGame m, SafeSocket sender) {
    	System.out.println(Utils.gson.toJson(m));
        return;
    }


    @Override
    void HandleJoinedGame(JoinedGame m, SafeSocket sender) {
        // TODO Auto-generated method stub

//    	System.out.println(Utils.gson.toJson(m));
    }

    /**
     * Server method
     */
    @Override
    void HandleNewGame(NewGame m, SafeSocket sender) {
//    	System.out.println(Utils.gson.toJson(m));
        return;
    }


    @Override
    void HandleAddLatLng(AddLatLng m, SafeSocket sender) {
        // TODO Auto-generated method stub
//    	System.out.println(Utils.gson.toJson(m));

    }


    @Override
    void HandleGameUpdate(GameUpdate m, SafeSocket sender) {
        // TODO Auto-generated method stub
//    	System.out.println(Utils.gson.toJson(m));

    }


    @Override
    void HandleVote(Vote m, SafeSocket sender) {
        // TODO Auto-generated method stub
//    	System.out.println(Utils.gson.toJson(m));

    }


}
