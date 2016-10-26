package spring.controllers;

import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public ResponseEntity<HttpResponseKo> handleException(Exception ex) {
        return new ResponseEntity<>(new HttpResponseKo(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadHttpRequest.class, HttpMessageNotReadableException.class})
    public
    @ResponseBody
    ResponseEntity<HttpResponseKo> handleException(BadHttpRequest ex) {
        return new ResponseEntity<>(new HttpResponseKo(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * GET /create  --> Create a new game and save it in the database.
     */
    @PostMapping(path = "/game/create", produces = "application/json", consumes="application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public HttpResponseOk<Game> create(@RequestBody Game game)
            throws BadHttpRequest {
        if (game == null)
            throw new BadHttpRequest();

        gameDao.save(game);

        return new HttpResponseOk<>(game);
    }


    /**
     * GET /delete  --> Delete the game having the passed id.
     */
    @DeleteMapping(path = "/game/delete", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Game> delete(int id) {
        Game game = new Game(id);
        gameDao.delete(game);

        return new HttpResponseOk<>(game);
    }

    /**
     * GET /get-by-email  --> Return the id for the game having the passed
     * email.
     */
    @GetMapping(path = "/game/get-by-name", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<List<Game>> getByName(String name) {
        List<Game> games = gameDao.findByName(name);

        return new HttpResponseOk<>(games);
    }

    /**
     * GET /update  --> Update the email and the name for the game in the
     * database having the passed id.
     */
    @PostMapping(path = "/game/update", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Game> updateGameName(long id, String name) {
        Game game = gameDao.findOne(id);
        game.setName(name);
        gameDao.save(game);

        return new HttpResponseOk<>(game);
    }
}
