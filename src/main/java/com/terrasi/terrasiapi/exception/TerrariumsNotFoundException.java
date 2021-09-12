package com.terrasi.terrasiapi.exception;

import com.terrasi.terrasiapi.controller.TerrariumController;

public class TerrariumsNotFoundException extends RuntimeException{

    public TerrariumsNotFoundException(){
        super("No terrariums were found");
    }
}
