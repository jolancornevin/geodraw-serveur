package spring.controllers;

import javassist.tools.web.BadHttpRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.utils.HttpResponseKo;

/**
 * Created by Djowood on 29/10/2016.
 */
public abstract class GeneriqueController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponseKo> handleException(Exception ex) {
        return new ResponseEntity<>(new HttpResponseKo(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadHttpRequest.class, HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity<HttpResponseKo> handleException(BadHttpRequest ex) {
        return new ResponseEntity<>(new HttpResponseKo(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<HttpResponseKo> handleException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(new HttpResponseKo(ex.getMessage()), HttpStatus.CONFLICT);
    }
}
