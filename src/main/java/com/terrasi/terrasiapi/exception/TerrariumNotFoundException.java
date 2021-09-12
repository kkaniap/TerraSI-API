package com.terrasi.terrasiapi.exception;

public class TerrariumNotFoundException extends RuntimeException {

    public TerrariumNotFoundException(long id) {
        super("Terrarium not found: " + id);
    }

    public TerrariumNotFoundException() {
        super("Terrarium not found");
    }
}
