package com.terrasi.terrasiapi.advices;

import com.terrasi.terrasiapi.exception.NewsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NewsNotFoundAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NewsNotFoundException.class)
    public String newsNotFound(NewsNotFoundException ex) {
        return ex.getMessage();
    }
}
