package com.terrasi.terrasiapi.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User with the given login or password does not exist");
    }
}
