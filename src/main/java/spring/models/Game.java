package spring.models;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by Djowood on 27/09/2016.
 */
@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private boolean locked;
    private int maxNbPlayer;
    private int currentNbPlayer;
    private Date startDate;
    private Date endDate;

    private String theme;

    /*private Map<String, Integer> players;
    private Map<String, Drawing> traces;
*/
    public Game(){

    }

    public Game(int id) {
        this.id = id;
    }

    public Game(int id, String name, Boolean lock, int currentNbPlayer, int maxNbPlayer, int hours, int minutes, String theme) {
        this.name = name;
        this.locked = lock;
        this.currentNbPlayer = currentNbPlayer;
        this.maxNbPlayer = maxNbPlayer;
        this.theme = theme;

        Calendar c = Calendar.getInstance();
        this.startDate = c.getTime();

        endDate = null;

        c.add(Calendar.HOUR, hours);
        c.add(Calendar.MINUTE, minutes);

        this.id = id;

        /*players = new HashMap<>();
        traces = new HashMap<>();*/
    }

    public long getId() {
        return id;
    }

    public Boolean getLock() {
        return locked;
    }

    public String getName() {
        return name;
    }

    public int getCurrentNbPlayer() {
        return currentNbPlayer;
    }

    /**
     * Adds a player to the current game
     *
     * @param playerID : The player's ID (pseudo)
     * @return true if the player has been successfully added, false otherwise
     */
    public boolean addPlayer(String playerID) {
        /*if (currentNbPlayer < maxNbPlayer) {
            if (players.containsKey(playerID))
                return true;
            players.put(playerID, 0);
            currentNbPlayer++;
            return true;
        }*/
        return false;
    }

    public void removePlayer(String playerID) {
        /*if (players.containsKey(playerID)) {
            players.remove(playerID);
            currentNbPlayer--;
        }*/
    }

    public boolean hasPlayer(String name) {
        //return players.containsKey(name);
        return false;
    }

    public Map<String, Integer> getPlayers() {
        //return players;
        return null;
    }

    public int getMaxNbPlayer() {
        return maxNbPlayer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getTheme() {
        return theme;
    }

    public Drawing getTrace(String playerID) {
        /*if (traces.containsKey(playerID))
            return traces.get(playerID);
*/
        return null;
    }

    public void updateTrace(String playerID, Drawing newTrace) {
        //traces.put(playerID, newTrace);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setMaxNbPlayer(int maxNbPlayer) {
        this.maxNbPlayer = maxNbPlayer;
    }

    public void setCurrentNbPlayer(int currentNbPlayer) {
        this.currentNbPlayer = currentNbPlayer;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setPlayers(Map<String, Integer> players) {
        //this.players = players;
    }

    public Map<String, Drawing> getTraces() {
        //return traces;
        return null;
    }

    public void setTraces(Map<String, Drawing> traces) {
        //this.traces = traces;
    }
}
