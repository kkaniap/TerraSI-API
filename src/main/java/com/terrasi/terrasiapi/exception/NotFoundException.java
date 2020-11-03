package com.terrasi.terrasiapi.exception;

public class NotFoundException extends Exception{
    public NotFoundException(){
        super("Not found");
    }
    public NotFoundException(String msg){
        super(msg);
    }
}
