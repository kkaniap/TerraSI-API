package com.terrasi.terrasiapi.exception;

public class NewsNotFoundException extends RuntimeException {

    public NewsNotFoundException(long id) {
        super("Could not find news: " + id);
    }

    public NewsNotFoundException() {
        super("Could not find news");
    }
}
