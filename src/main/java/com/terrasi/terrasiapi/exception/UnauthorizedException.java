package com.terrasi.terrasiapi.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(){
        super("User unauthorized");
    }
}
