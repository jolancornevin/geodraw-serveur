package fr.insa.ot3.communication.message;


public class NewGame extends Message {
    private final String name;
    private final boolean lock;
    private final int maxNbPlayer;
    private final int minDur;
    private final int hoursDur;
    private final String theme;

    private final String playerID;

    public NewGame(String name, boolean lock, int maxNbPlayer, int hours, int min, String theme, String playerID) {
        super(Type.NEWGAME);
        this.name = name;
        this.lock = lock;
        this.maxNbPlayer = maxNbPlayer;
        this.minDur = min;
        this.hoursDur = hours;
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

    public int getHours() {
        return hoursDur;
    }

    public int getMins() {
        return minDur;
    }

    public String getTheme() {
        return theme;
    }

    public String getPlayerID() {
        return playerID;
    }
}
