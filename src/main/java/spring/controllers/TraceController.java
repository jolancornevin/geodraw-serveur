package spring.controllers;

import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spring.daos.PlayerDao;
import spring.daos.TraceDao;
import spring.models.Trace;
import spring.utils.HttpResponseOk;

/**
 * Created by Djowood on 25/10/2016.
 */
@Controller
public class TraceController extends GeneriqueController {

    // Private fields

    @Autowired
    private TraceDao traceDao;

    public TraceDao getTraceDao() {
        return traceDao;
    }

    @Autowired
    private PlayerDao playerDao;

    /**
     * GET /create  --> Create a new game and save it in the database.
     */
    @PostMapping(path = "/trace/create", produces = "application/json", consumes = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public HttpResponseOk<Trace> create(@RequestBody Trace trace)
            throws BadHttpRequest {
        if (trace == null)
            throw new BadHttpRequest();

        trace = traceDao.save(trace);

        return new HttpResponseOk<>(trace);
    }

    /**
     * GET /delete  --> Delete the game having the passed id.
     */
    @DeleteMapping(path = "/trace/delete", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public HttpResponseOk<Trace> delete(Long id) {
        Trace trace = new Trace(id);
        traceDao.delete(trace);

        return new HttpResponseOk<>(null);
    }
}
