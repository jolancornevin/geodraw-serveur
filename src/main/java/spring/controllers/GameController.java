package spring.controllers;

import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.daos.GameDao;
import spring.daos.PlayerDao;
import spring.models.Game;
import spring.models.Player;
import spring.utils.HttpResponseOk;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by Djowood on 25/10/2016.
 */
@Controller
@Transactional
public class GameController extends GeneriqueController {

    // Private fields

    @Autowired
    private GameDao gameDao;

    public GameDao getGameDao() {
        return gameDao;
    }

    @Autowired
    private PlayerDao playerDao;

    /**
     * GET /create  --> Create a new game and save it in the database.
     */
    @PostMapping(path = "/game/create", produces = "application/json", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public HttpResponseOk<Game> _create(@RequestBody Game game)
            throws BadHttpRequest {
        game = create(game);

        return new HttpResponseOk<>(game);
    }

    public Game create(Game game) throws BadHttpRequest {
        if (game == null) throw new BadHttpRequest();

        return gameDao.save(game);
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

    /**
     * GET /getAll  --> Get a get by ID.
     */
    @GetMapping(path = "/game/getAll", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<List<Game>> _getAll() throws BadHttpRequest {
        List<Game> games = getAllWithPlayersAndTraces();

        return new HttpResponseOk<>(games);
    }

    @PostMapping(path = "/game/joinGame", produces = "application/json", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Game> _joinGame(@RequestBody Map<String, Long> json)
            throws BadHttpRequest, DataIntegrityViolationException {
        Long idGame, idPlayer;
        Game game;

        //Get params
        idGame = json.get("idGame");
        idPlayer = json.get("idPlayer");

        game = joinGame(idGame, idPlayer);

        return new HttpResponseOk<>(game);
    }

    public Game joinGame(Long idGame, Long idPlayer) throws BadHttpRequest {
        Player player;
        Game game;
        if (idGame == null || idPlayer == null) throw new BadHttpRequest();

        //Bad request because the id doesn't exist
        if ((game = gameDao.findOne(idGame)) == null) throw new BadHttpRequest();
        //Bad request because the id doesn't exist
        if ((player = playerDao.findOne(idPlayer)) == null) throw new BadHttpRequest();

        if (game.hasPlayer(player))
            return game;
        else {
            //The game is complete
            if ((game.getCurrentNbPlayer() + 1) > game.getMaxNbPlayer())
                throw new DataIntegrityViolationException("The game is complete");

            game.addPlayer(player);

            return gameDao.save(game);
        }
    }

    @PostMapping(path = "/game/leaveGame", produces = "application/json", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Game> leaveGame(@RequestBody Map<String, Long> json)
            throws BadHttpRequest, DataIntegrityViolationException {
        Game game;
        Player player;
        Long idGame, idPlayer;

        //Get params
        idGame = json.get("idGame");
        idPlayer = json.get("idPlayer");

        if (idGame == null || idPlayer == null) throw new BadHttpRequest();

        //Bad request because the id doesn't exist
        if ((game = gameDao.findOne(idGame)) == null) throw new BadHttpRequest();
        //Bad request because the id doesn't exist
        if ((player = playerDao.findOne(idPlayer)) == null) throw new BadHttpRequest();

        //The game is complete
        if (!game.hasPlayer(player))
            throw new DataIntegrityViolationException("The player does not belong to the game");

        game.removePlayer(player);
        game = gameDao.save(game);

        return new HttpResponseOk<>(game);
    }

    /**
     * Récupère la liste des games sur le serveur (avec les traces et les players)
     * Ensuite, comme les traces sont stocké en JSON pour le moment, on les parse pour les mettre en objet
     *
     * @return
     */
    public List<Game> getAllWithPlayersAndTraces() {
        List<Game> games = (List) gameDao.findAll();
        for(Game game : games){
            game.setTracesFromBd();
        }
        return games;
    }

    public Game save(Game game) {
        //Set traces to be save
        game.setBdTraces();

        return gameDao.save(game);
    }
}
