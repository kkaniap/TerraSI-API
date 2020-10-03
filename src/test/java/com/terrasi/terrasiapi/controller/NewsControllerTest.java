package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.model.News;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Test
    void shouldGetAllNews() throws Exception {
        //given
        MvcResult result = mockMvc.perform(
            get("/news")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Traverson traverson = new Traverson(
                new URI("http://localhost:" + port + linkTo(NewsController.class)), MediaTypes.HAL_JSON);

        //when
        PagedModel<News> news = traverson.follow("self")
                .toObject(new ParameterizedTypeReference<>(){});

        //then
        assertEquals(3, news.getContent().size());
    }

    @Test
    void shouldThrow406Status() throws Exception {
        MvcResult result = mockMvc.perform(
            get("/news")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE))
            .andExpect(status().isNotAcceptable())
            .andReturn();
    }

    @Test
    void shouldGetNewsById() throws Exception {
        //given
        MvcResult result = mockMvc.perform(
                get("/news/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Traverson traverson = new Traverson(new URI("http://localhost:" + port + linkTo(NewsController.class)
                .slash("1")),
                MediaTypes.HAL_JSON);

        //when
        News news = traverson.follow("self").toObject(News.class);

        //then
        assertEquals(1L, news.getId());
    }

    @Test
    void shouldNotGetNewsById() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/news/100")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }
}



