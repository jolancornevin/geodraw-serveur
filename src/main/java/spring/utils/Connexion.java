package spring.utils;

import spring.controllers.GameController;
import spring.controllers.PlayerController;
import spring.daos.GameDao;
import spring.daos.PlayerDao;

/**
 * Created by Djowood on 04/11/2016.
 */
public class Connexion {
    private PlayerDao playerDao;
    private PlayerController playerController;

    private GameDao gameDao;
    private GameController gameController;

    private Connexion(PlayerController pc, PlayerDao pd, GameController gc, GameDao gd) {
        playerController = pc;
        playerDao = pd;
        gameController = gc;
        gameDao = gd;
    }

    private static Connexion instance;

    public static Connexion getInstance() {
        return instance;
    }

    public static void setInstance(PlayerController pc, PlayerDao pd, GameController gc, GameDao gd) {
        instance = new Connexion(pc, pd, gc, gd);
    }

    public PlayerDao getPlayerDao() {
        return playerDao;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public GameController getGameController() {
        return gameController;
    }
}
