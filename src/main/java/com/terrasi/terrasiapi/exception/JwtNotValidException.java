package com.terrasi.terrasiapi.exception;

public class JwtNotValidException extends RuntimeException {

    public JwtNotValidException() {
        super("Jwt token is not valid");
    }
}
