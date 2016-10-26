package spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.daos.GameDao;
import spring.models.Game;
import spring.utils.HttpResponseKo;
import spring.utils.HttpResponseOk;

import java.util.List;

/**
 * Created by Djowood on 25/10/2016.
 */
@Controller
public class GameController {

    // Private fields

    @Autowired
    private GameDao gameDao;

    @ExceptionHandler(Exception.class)
    public HttpResponseKo handleException(Exception ex) {
        return new HttpResponseKo(ex.getMessage());
    }

    /**
     * GET /create  --> Create a new game and save it in the database.
     */
    @PostMapping(path = "/game/create", produces = "application/json")
    @ResponseBody
    public HttpResponseOk<Game> create(String name) {
        Game game = new Game(2, "coucou2", false, 0, 20, 5, 5, "avion");
        gameDao.save(game);

        return new HttpResponseOk<>(game);
    }

    /**
     * GET /delete  --> Delete the game having the passed id.
     */
    @DeleteMapping("/game/delete")
    @ResponseBody
    public HttpResponseOk<Game> delete(int id) {
        Game game = new Game(id);
        gameDao.delete(game);

        return new HttpResponseOk<>(game);
    }

    /**
     * GET /get-by-email  --> Return the id for the game having the passed
     * email.
     */
    @RequestMapping(path = "/game/get-by-name", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponseOk<List<Game>> getByName(String name) {
        List<Game> games = gameDao.findByName(name);

        return new HttpResponseOk<>(games);
    }

    /**
     * GET /update  --> Update the email and the name for the game in the
     * database having the passed id.
     */
    @PostMapping("/game/update")
    @ResponseBody
    public HttpResponseOk<Game> updateGameName(long id, String name) {
        Game game = gameDao.findOne(id);
        game.setName(name);
        gameDao.save(game);

        return new HttpResponseOk<>(game);
    }
}
