package com.terrasi.terrasiapi.exception;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(){
        super("Unauthorized");
    }
}
