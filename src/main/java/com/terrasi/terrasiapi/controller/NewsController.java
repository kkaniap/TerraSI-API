package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.exception.NewsNotFoundException;
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

    private final NewsService newsService;
    private final NewsRepository newsRepository;
    private final PagedResourcesAssembler<News> pagedResourcesAssembler;

    public NewsController(NewsRepository newsRepository, NewsService newsService, PagedResourcesAssembler<News> pagedResourcesAssembler) {
        this.newsService = newsService;
        this.newsRepository = newsRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<News>> getAllNews(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable page) {
        Page<News> news = newsRepository.findAll(page);
        if (news.getTotalElements() > 0) {
            news.forEach(n -> n.add(linkTo(methodOn(NewsController.class).getNews(n.getId())).withSelfRel()));
            PagedModel<News> newsModel = pagedResourcesAssembler.toModel(news, n -> n);
            return new ResponseEntity<>(newsModel, HttpStatus.OK);
        }
        throw new NewsNotFoundException();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<News> getNews(@PathVariable Long id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            news.get().setContent(newsService.readNews(id));
            news.get().add(linkTo(methodOn(NewsController.class).getNews(id)).withSelfRel());
            return new ResponseEntity<>(news.get(), HttpStatus.OK);
        }
        throw new NewsNotFoundException(id);
    }
}

