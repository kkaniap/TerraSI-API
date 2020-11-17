package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.Utils.JwtUtils;
import com.terrasi.terrasiapi.exception.ForbiddenException;
import com.terrasi.terrasiapi.exception.NotFoundException;
import com.terrasi.terrasiapi.exception.UnauthorizedException;
import com.terrasi.terrasiapi.model.JwtModel;
import com.terrasi.terrasiapi.model.Terrarium;
import com.terrasi.terrasiapi.model.TerrariumSettings;
import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.repository.TerrariumRepository;
import com.terrasi.terrasiapi.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/terrariums", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class TerrariumController {

    private final TerrariumRepository terrariumRepository;
    private final TerrariumService terrariumService;
    private final PagedResourcesAssembler<Terrarium> pagedResourcesAssembler;

    public TerrariumController(TerrariumRepository terrariumRepository,TerrariumService terrariumService, PagedResourcesAssembler<Terrarium> pagedResourcesAssembler) {
        this.terrariumRepository = terrariumRepository;
        this.terrariumService = terrariumService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<Terrarium>> getAllTerrariumsByUser(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page,
            @RequestHeader("Authorization") String accessToken){

        Page<Terrarium> terrariums = terrariumService.getTerrariumsByToken(accessToken, page);

        if(terrariums != null && terrariums.getTotalElements() > 0){
            terrariums.forEach(t -> t.add(linkTo(methodOn(TerrariumController.class).getTerrariumById(t.getId(),""))
                    .withSelfRel()));
            PagedModel<Terrarium> terrariumModel = pagedResourcesAssembler.toModel(terrariums, t -> t);
            return new ResponseEntity<>(terrariumModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getTerrariumById(@PathVariable Long id, @RequestHeader("Authorization") String accessToken){
        Terrarium terrarium;
        try{
            terrarium = terrariumService.getTerrariumById(id, accessToken);
        }catch (UnauthorizedException e){
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e){
            return new ResponseEntity<>("User or terrarium not found", HttpStatus.NOT_FOUND);
        }catch (ForbiddenException e){
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
        terrarium.add(linkTo(methodOn(TerrariumController.class).getTerrariumById(id,"")).withSelfRel());
        return new ResponseEntity<>(terrarium, HttpStatus.OK);
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<String> saveTerrariumSettings(@PathVariable Long id, @RequestBody TerrariumSettings settings,
                                                        @RequestHeader("Authorization") String accessToken){
        try{
            terrariumService.saveTerrariumSettings(id, accessToken, settings);
        }catch (UnauthorizedException e){
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User or terrarium not found", HttpStatus.NOT_FOUND);
        }catch (ForbiddenException e){
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
        System.out.println(settings);
        return new ResponseEntity<>("Settings updated", HttpStatus.OK);
    }
}


