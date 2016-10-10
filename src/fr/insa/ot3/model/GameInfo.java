package fr.insa.ot3.model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Djowood on 27/09/2016.
 */

public class GameInfo {
	
    private final int id;
    private final String name;
    private final boolean lock;
	
    private int currentNbPlayer;
    private final int maxNbPlayer;
    private final int remainingHours;
    private final int remainingMinutes;
	
    private final int maxHours;
    private final int maxMinutes;
	
    private final String theme;

	
	private final static long nbMilliHour = 1000 * 60 * 60;
	private final static long nbMilliMinute = 1000 * 60;

    public GameInfo(Game g) {
        this.name = g.getName();
        this.lock = g.getLock();
        this.currentNbPlayer = g.getCurrentNbPlayer();
        this.maxNbPlayer = g.getMaxNbPlayer();
        this.theme = g.getTheme();
        
		Date current = Calendar.getInstance().getTime();
		Date start = g.getStartDate();
		
		Date end = g.getEndDate();
		
		remainingHours = (end.getTimeInMillis() - current.getTimeInMillis()) / nbMilliHour;
		remainingMinutes = ((end.getTimeInMillis() - current.getTimeInMillis()) % nbMilliHour) / nbMilliMinute;
		
		maxHours = (end.getTimeInMillis() - start.getTimeInMillis()) / nbMilliHour;
		maxMinutes = ((end.getTimeInMillis() - start.getTimeInMillis()) % nbMilliHour) / nbMilliMinute;
		
		
        this.id = id;
		
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

    public int getMaxNbPlayer() {
        return maxNbPlayer;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public int getMaxMinutes() {
        return maxMinutes;
    }

    public int getRemainingHours() {
        return remainingHours;
    }

    public int getRemainingMinutes() {
        return remainingMinutes;
    }
	
    public String getTheme() {
        return theme;
    }
}
