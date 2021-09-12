package com.terrasi.terrasiapi.advices;

import com.terrasi.terrasiapi.exception.TerrariumNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TerrariumNotFoundAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TerrariumNotFoundException.class)
    public String jwtExpired(TerrariumNotFoundException ex) {
        return ex.getMessage();
    }
}
