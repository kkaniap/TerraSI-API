package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.model.News;
import com.terrasi.terrasiapi.repository.NewsRepository;
import com.terrasi.terrasiapi.service.NewsService;
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

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/news", produces = {MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE})
public class NewsController {

    private final NewsRepository newsRepository;
    private final NewsService newsService;
    private final PagedResourcesAssembler<News> pagedResourcesAssembler;

    public NewsController(NewsRepository newsRepository, NewsService newsService, PagedResourcesAssembler<News> pagedResourcesAssembler) {
        this.newsRepository = newsRepository;
        this.newsService = newsService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<News>> getAllNews(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page){
        Page<News> news = newsRepository.findAll(page);
        if(news.getTotalElements() > 0){
            news.forEach(n -> n.add(linkTo(methodOn(NewsController.class).getNews(n.getId())).withSelfRel()));
            PagedModel<News> newsModel =  pagedResourcesAssembler.toModel(news, entity -> entity);
            return new ResponseEntity<PagedModel<News>>(newsModel, HttpStatus.OK);
        }
        return  new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<News> getNews(@PathVariable Long id){
        Optional<News> news = newsRepository.findById(id);
        if(news.isPresent()){
            news.get().setContent(newsService.readNews(id));
            news.get().add(linkTo(methodOn(NewsController.class).getNews(id)).withSelfRel());
            return new ResponseEntity<News>(news.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}

