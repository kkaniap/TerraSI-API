package com.terrasi.terrasiapi.exception;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(String msg){
        super(msg);
    }
    public UnauthorizedException(){
        super("Unauthorized");
    }
}
