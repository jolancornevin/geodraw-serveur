package src.main.java.fr.insa.ot3.communication.message;

import java.util.Date;

public class NewGame extends Message
{
	private final String name;
    private final boolean lock;
    private final int maxNbPlayer;
    private final Date startDate;
    private final Date endDate;
    private final String theme;
    
    private final String playerID;
    
	public NewGame(String name, boolean lock, int maxNbPlayer, Date startDate, Date endDate, String theme, String playerID)
	{
		super(Type.NEWGAME);
		this.name = name;
		this.lock = lock;
		this.maxNbPlayer = maxNbPlayer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.theme = theme;
		
		this.playerID = playerID;
	}

	public String getName() {
		return name;
	}

	public boolean isLock() {
		return lock;
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

	public String getPlayerID() {
		return playerID;
	}
}
