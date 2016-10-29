package spring.controllers;

import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.Map;

/**
 * Created by Djowood on 25/10/2016.
 */
@Controller
public class GameController extends GeneriqueController{

    // Private fields

    @Autowired
    private GameDao gameDao;

    /**
     * GET /create  --> Create a new game and save it in the database.
     */
    @PostMapping(path = "/game/create", produces = "application/json", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public HttpResponseOk<Game> create(@RequestBody Game game)
            throws BadHttpRequest {
        if (game == null)
            throw new BadHttpRequest();

        game = gameDao.save(game);

        return new HttpResponseOk<>(game);
    }

    /**
     * GET /delete  --> Delete the game having the passed id.
     */
    @DeleteMapping(path = "/game/delete", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Game> delete(Long id) {
        Game game = new Game(id);
        gameDao.delete(game);

        return new HttpResponseOk<>(null);
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
     * GET /get  --> Get a get by ID.
     */
    @GetMapping(path = "/game/get", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Game> get(Long id) throws BadHttpRequest {
        if (id == null)
            throw new BadHttpRequest();

        Game game = gameDao.findOne(id);

        return new HttpResponseOk<>(game);
    }


    @PostMapping(path = "/game/joinGame", produces = "application/json", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Game> joinGame(@RequestBody Map<String, Long> json)
            throws BadHttpRequest, DataIntegrityViolationException {
        //Get params
        Long idGame = json.get("idGame"), idPlayer = json.get("idPlayer");

        if (idGame == null || idPlayer == null) throw new BadHttpRequest();

        Game game = gameDao.findOne(idGame);

        //Bad request because the id doesn't exist
        if (game == null) throw new BadHttpRequest();

        int nbPlayers = game.getCurrentNbPlayer() + 1;

        //The game is complete
        if (nbPlayers > game.getMaxNbPlayer()) throw new DataIntegrityViolationException("The game is complete");

        game.setCurrentNbPlayer(nbPlayers);
        game = gameDao.save(game);

        return new HttpResponseOk<>(game);
    }

    @PostMapping(path = "/game/leaveGame", produces = "application/json", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Game> leaveGame(@RequestBody Map<String, Long> json)
            throws BadHttpRequest, DataIntegrityViolationException {
        //Get params
        Long idGame = json.get("idGame"), idPlayer = json.get("idPlayer");

        if (idGame == null || idPlayer == null) throw new BadHttpRequest();

        Game game = gameDao.findOne(idGame);

        //Bad request because the id doesn't exist
        if (game == null) throw new BadHttpRequest();

        int nbPlayers = game.getCurrentNbPlayer() - 1;

        game.setCurrentNbPlayer(nbPlayers);
        game = gameDao.save(game);

        return new HttpResponseOk<>(game);
    }
}
