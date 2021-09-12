package com.terrasi.terrasiapi.exception;

public class JwtExpiredException extends RuntimeException {

    public JwtExpiredException() {
        super("Jwt token is expired");
    }
}
