/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spring.models;

import spring.communication.Client;
import spring.communication.Server;
import spring.communication.message.AddLatLng;
import spring.communication.message.NewGame;
import spring.utils.Utils;

import java.util.Calendar;
import java.util.Date;


/**
 * @author max
 */
public class GeoDrawModel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Segment segment1 = new Segment();
        segment1.addLatLng(new LatLng(0.234534, 0.234523));
        segment1.addLatLng(new LatLng(0.23452345, 0.234523));
        segment1.addLatLng(new LatLng(0.2323434, 0.45623));
        segment1.addLatLng(new LatLng(0.45645, 0.678));

        Segment segment2 = new Segment();
        segment2.addLatLng(new LatLng(0.7654534, 0.67234523));
        segment2.addLatLng(new LatLng(0.567452345, 0.67234523));
        segment2.addLatLng(new LatLng(0.456323434, 0.8745623));
        segment2.addLatLng(new LatLng(0.5578645, 0.567678));

        Drawing trace = new Drawing();
        trace.addSegment(segment1);
        trace.addSegment(segment2);

        Client c = new Client("localhost", 8080);
        c.sendMessage(new AddLatLng("dfh", 1, new LatLng(0.234534, 0.234523), true));

//        Gson gson = new Gson();
//        String jsonString = gson.toJson(trace);
//        
//        System.out.println(jsonString);
//        
//        TraceMessage t = new TraceMessage(trace);
//        jsonString = gson.toJson(t);
//        System.out.println(jsonString);
//        
//        Message tt = Message.parseMessage(jsonString);
//
//        System.out.println(tt.getId());
//        jsonString = gson.toJson(tt);
//        System.out.println(jsonString);

//        Server s = new Server();

        Calendar cal = Calendar.getInstance();
        Date start = cal.getTime();
        cal.add(Calendar.HOUR, 2);
        Date end = cal.getTime();
//        
//        Game g = new Game(0, "patate", false, 0, 15, start, end, "Les avions");
//        Game g2 = new Game(1, "patate", false, 0, 15, start, end, "Les avions");
//        
//        g2.addPlayer("bob");
//        g2.addPlayer("alice");
//        System.out.println(Utils.gson.toJson(g));
//        System.out.println(Utils.gson.toJson(g2));
//        
        Server s = new Server();
//      System.out.println(Utils.gson.toJson(s));

        s.start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Client a = new Client("localhost", 8080);
        c.sendMessage(new NewGame("patate", false, 15, 5, 2, "Les avions", "franck"));

        c.disconnect();
        s.stop();
        String jsonstr = Utils.gson.toJson(s);

        Server s2 = Utils.gson.fromJson(jsonstr, Server.class);
        System.out.println(Utils.gson.toJson(s2));

    }

}
