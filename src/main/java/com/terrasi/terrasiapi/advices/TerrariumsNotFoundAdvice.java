package com.terrasi.terrasiapi.advices;

import com.terrasi.terrasiapi.exception.TerrariumsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TerrariumsNotFoundAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TerrariumsNotFoundException.class)
    public String jwtExpired(TerrariumsNotFoundException ex) {
        return ex.getMessage();
    }
}
