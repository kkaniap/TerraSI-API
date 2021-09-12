package com.terrasi.terrasiapi.advices;

import com.terrasi.terrasiapi.exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ForbiddenAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public String jwtExpired(ForbiddenException ex) {
        return ex.getMessage();
    }
}
