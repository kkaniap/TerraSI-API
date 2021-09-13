package com.terrasi.terrasiapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.terrasi.terrasiapi.exception.*;
import com.terrasi.terrasiapi.model.SensorsReads;
import com.terrasi.terrasiapi.model.Terrarium;
import com.terrasi.terrasiapi.model.TerrariumSettings;
import com.terrasi.terrasiapi.service.TerrariumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/terrariums", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class TerrariumController {

    private final TerrariumService terrariumService;
    private final PagedResourcesAssembler<Terrarium> pagedResourcesAssembler;

    public TerrariumController(TerrariumService terrariumService, PagedResourcesAssembler<Terrarium> pagedResourcesAssembler) {
        this.terrariumService = terrariumService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<Terrarium>> getAllTerrariumsByUser(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable page,
            @RequestHeader("Authorization") String accessToken) {

        Page<Terrarium> terrariums = terrariumService.getTerrariumsByToken(accessToken, page);

        if (terrariums.getTotalElements() > 0) {
            terrariums.forEach(t -> t.add(linkTo(methodOn(TerrariumController.class).getTerrariumById(t.getId(), "")).withSelfRel()));
            PagedModel<Terrarium> terrariumModel = pagedResourcesAssembler.toModel(terrariums, t -> t);
            return new ResponseEntity<>(terrariumModel, HttpStatus.OK);
        }
        throw new TerrariumsNotFoundException();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getTerrariumById(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        Terrarium terrarium = terrariumService.getTerrariumById(id, accessToken);
        terrarium.add(linkTo(methodOn(TerrariumController.class).getTerrariumById(id, "")).withSelfRel());
        return new ResponseEntity<>(terrarium, HttpStatus.OK);
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<String> saveTerrariumSettings(@PathVariable Long id, @RequestBody TerrariumSettings settings,
                                                        @RequestHeader("Authorization") String accessToken) {

        if (terrariumService.saveTerrariumSettings(id, accessToken, settings)) {
            try {
                terrariumService.sendTerrariumSettings(settings, accessToken);
            } catch (JsonProcessingException ex) {
                return new ResponseEntity<>("Internal server error during parsing to json", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>("Settings updated", HttpStatus.OK);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<String> updateTerrariumName(@PathVariable Long id, @RequestBody String name,
                                                      @RequestHeader("Authorization") String accessToken) {
        terrariumService.updateTerrariumName(id, name, accessToken);
        return new ResponseEntity<>("Name updated", HttpStatus.OK);
    }

    @PostMapping("/{id}/bulbOnOf")
    public ResponseEntity<Object> turnOnOffBulb(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        try {
            terrariumService.bulbTurnOnOf(id, accessToken);
        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>("Internal server error during parsing to json", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Name updated", HttpStatus.OK);
    }

    @PostMapping("/{id}/humidifierOnOff")
    public ResponseEntity<Object> turnOnOffHumidifier(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        try {
            terrariumService.humidifierTurnOnOf(id, accessToken);
        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>("Internal server error during parsing to json", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Name updated", HttpStatus.OK);
    }

    @GetMapping("/{id}/sensorsReads")
    public ResponseEntity<Object> getSensorReads(@PathVariable Long id, @RequestHeader("Authorization") String accessToken){
        SensorsReads sensorsReads;
        try {
            sensorsReads = this.terrariumService.getSensorSReads(id, accessToken);
        } catch (IOException e) {
            return new ResponseEntity<>("Internal server error during parsing json", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(sensorsReads, HttpStatus.OK);
    }
}


