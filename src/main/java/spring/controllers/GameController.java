package spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import spring.daos.GameDao;

import java.util.List;

/**
 * Created by Djowood on 25/10/2016.
 */
@Controller
public class GameController {

    // Private fields

    @Autowired
    private GameDao gameDao;

    /**
     * GET /create  --> Create a new game and save it in the database.
     */
    @PostMapping("/game/create")
    @ResponseBody
    public String create(String name) {
        String gameId = "";
        try {
            Game game = new Game(1, "coucou", false, 0, 20, 5, 5, "avion");
            gameDao.save(game);
        } catch (Exception ex) {
            return "Error creating the game: " + ex.toString();
        }
        return "game succesfully created with id = " + gameId;
    }

    /**
     * GET /delete  --> Delete the game having the passed id.
     */
    @DeleteMapping("/game/delete")
    @ResponseBody
    public String delete(int id) {
        try {
            Game game = new Game(id);
            gameDao.delete(game);
        } catch (Exception ex) {
            return "Error deleting the game:" + ex.toString();
        }
        return "game succesfully deleted!";
    }

    /**
     * GET /get-by-email  --> Return the id for the game having the passed
     * email.
     */
    @RequestMapping(path = "/game/get-by-name", method = RequestMethod.GET)
    @ResponseBody
    public String getByName(String name) {
        String gameId = "";
        try {
            List<Game> games = gameDao.findByName(name);
            gameId = String.valueOf(games.get(0).getId());
        } catch (Exception ex) {
            return "Game not found";
        }
        return "The game id of the first one is: " + gameId;
    }

    /**
     * GET /update  --> Update the email and the name for the game in the
     * database having the passed id.
     */
    @PostMapping("/game/update")
    @ResponseBody
    public String updateGameName(long id, String name) {
        try {
            Game game = gameDao.findOne(id);
            game.setName(name);
            gameDao.save(game);
        } catch (Exception ex) {
            return "Error updating the game: " + ex.toString();
        }
        return "game succesfully updated!";
    }
}
