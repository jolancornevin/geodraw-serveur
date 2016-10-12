package fr.insa.ot3.model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Djowood on 27/09/2016.
 */

public class Game {
	
    private final int id;
    private final String name;
    private final boolean lock;
    private final int maxNbPlayer;
    private final Date startDate;
    private final Date endDate;
	
    private final String theme;

    private final Map<String, Integer> players;
    private final Map<String, Drawing> traces;
    
    private int currentNbPlayer;

    public Game(int id, String name, Boolean lock, int currentNbPlayer, int maxNbPlayer, int hours, int minutes, String theme) {
        this.name = name;
        this.lock = lock;
        this.currentNbPlayer = currentNbPlayer;
        this.maxNbPlayer = maxNbPlayer;
        this.theme = theme;
        
		Calendar c = Calendar.getInstance();
		startDate = Calendar.getTime();
		
		c.add(Calendar.HOUR, hours);
		c.add(Calendar.MINUTE, minutes);
		
		
        this.id = id;
        
        players = new HashMap<String, Integer>();
        traces = new HashMap<String, Drawing>();
    }

    public int getId() {
        return id;
    }

    public Boolean getLock() {
        return lock;
    }

    public String getName() {
        return name;
    }

    public int getCurrentNbPlayer() {
        return currentNbPlayer;
    }

    /**
     * Adds a player to the current game
     * @param playerID : The player's ID (pseudo)
     * @return true if the player has been successfully added, false otherwise
     */
    public boolean addPlayer(String playerID) {
        if(currentNbPlayer < maxNbPlayer)
        {
        	if(players.containsKey(playerID))
        		return true;
        	players.put(playerID, 0);
        	currentNbPlayer++;
        	return true;
        }
        return false;
    }
    
    public void removePlayer(String playerID) {
    	if(players.containsKey(playerID))
    	{
    		players.remove(playerID);
    		currentNbPlayer--;
    	}
    }
	
	public boolean hasPlayer(String name)
	{
		return players.containsKey(name);
	}
	
	public Map<String, Integer> getPlayers()
	{
		return players;
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
    
    public Drawing getTrace(String playerID)
    {
    	if(traces.containsKey(playerID))
    		return traces.get(playerID);
    	
    	return null;
    }
    
    public void updateTrace(String playerID, Drawing newTrace){
    	traces.put(playerID, newTrace);
    }
}
