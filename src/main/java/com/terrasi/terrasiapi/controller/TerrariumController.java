package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.model.Terrarium;
import com.terrasi.terrasiapi.repository.TerrariumRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(path = "/terrariums", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class TerrariumController {

    private final TerrariumRepository terrariumRepository;
    private final PagedResourcesAssembler<Terrarium> pagedResourcesAssembler;

    public TerrariumController(TerrariumRepository terrariumRepository,
                               PagedResourcesAssembler<Terrarium> pagedResourcesAssembler) {
        this.terrariumRepository = terrariumRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<Terrarium>> getAllTerrariumsByUser(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable page){
        Page<Terrarium> terrariums = terrariumRepository.getAllByUserId(2L, page);
        if(terrariums.getTotalElements() > 0){
            terrariums.forEach(t -> t.add(linkTo(methodOn(TerrariumController.class).getTerrariumById(t.getId()))
                    .withSelfRel()));
            PagedModel<Terrarium> terrariumModel = pagedResourcesAssembler.toModel(terrariums, t -> t);
            return new ResponseEntity<PagedModel<Terrarium>>(terrariumModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Terrarium> getTerrariumById(@PathVariable Long id){
        Optional<Terrarium> terrarium = terrariumRepository.findById(id);
        if(terrarium.isPresent()){
            terrarium.get().add(linkTo(methodOn(TerrariumController.class).getTerrariumById(id)).withSelfRel());
            return new ResponseEntity<Terrarium>(terrarium.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}


//TODO 1 zmienic id w argumentach na id zalogowanego usera
