package com.terrasi.terrasiapi.advices;

import com.terrasi.terrasiapi.exception.JwtNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class JwtNotValidAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtNotValidException.class)
    public String jwtNotValid(JwtNotValidException ex) {
        return ex.getMessage();
    }
}
