package com.terrasi.terrasiapi.exception;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(){
        super("Access forbidden");
    }
}
