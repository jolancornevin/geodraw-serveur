package spring.controllers;

import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.daos.PlayerDao;
import spring.models.Player;
import spring.utils.HttpResponseOk;

/**
 * Created by Djowood on 25/10/2016.
 */
@Controller
public class PlayerController extends GeneriqueController {

    // Private fields

    @Autowired
    private PlayerDao playerDao;

    public PlayerDao getPlayerDao() {
        return playerDao;
    }

    /**
     * GET /create  --> Create a new player and save it in the database.
     */
    @PostMapping(path = "/player/create", produces = "application/json", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public HttpResponseOk<Player> create(@RequestBody Player player) throws BadHttpRequest {
        if (player == null)
            throw new BadHttpRequest();

        player = playerDao.save(player);

        return new HttpResponseOk<>(player);
    }

    /**
     * GET /create  --> Create a new player and save it in the database.
     */
    @GetMapping(path = "/player/get", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Player> get(Long id) throws BadHttpRequest {
        if (id == null)
            throw new BadHttpRequest();

        Player player = playerDao.findOne(id);

        return new HttpResponseOk<>(player);
    }

    /**
     * GET /delete  --> Delete the player having the passed id.
     */
    @DeleteMapping(path = "/player/delete", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Player> delete(Long id) {
        Player player = new Player(id);
        playerDao.delete(player);

        return new HttpResponseOk<>(null);
    }

}
