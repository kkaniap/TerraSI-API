package com.terrasi.terrasiapi.advices;

import com.terrasi.terrasiapi.exception.JwtExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class JwtExpiredAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtExpiredException.class)
    public String jwtExpired(JwtExpiredException ex) {
        return ex.getMessage();
    }
}
