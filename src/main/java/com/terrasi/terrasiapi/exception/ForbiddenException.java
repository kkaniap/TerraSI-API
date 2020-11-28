package com.terrasi.terrasiapi.exception;

public class ForbiddenException extends Exception{
    public ForbiddenException(){
        super("Access forbidden");
    }
}
