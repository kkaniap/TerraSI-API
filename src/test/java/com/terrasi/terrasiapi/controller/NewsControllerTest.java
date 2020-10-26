package com.terrasi.terrasiapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@AutoConfigureMockMvc
@TestPropertySource(locations = "/application-dev.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Test
    void shouldGetAllNews() throws Exception {
        //given
        String token = "Bearer " + obtainAccessToken();
        System.out.println(token);
        MvcResult result = mockMvc.perform(
            get("/news")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Traverson traverson = new Traverson(
                new URI("http://localhost:" + port + linkTo(NewsController.class)), MediaTypes.HAL_JSON);
        Traverson.TraversalBuilder traversalBuilder = traverson.follow("self");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        //when
        PagedModel<News> news = traversalBuilder.withHeaders(headers).toObject(new ParameterizedTypeReference<>(){});

        //then
        assertEquals(4, news.getContent().size());
    }

    @Test
    void shouldThrow406Status() throws Exception {
        String token = "Bearer " + obtainAccessToken();
        mockMvc.perform(
            get("/news")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE))
            .andExpect(status().isNotAcceptable())
            .andReturn();
    }

    @Test
    void shouldThrow403Status() throws Exception {
        mockMvc.perform(
            get("/news")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetNewsById() throws Exception {
        //given
        String token = "Bearer " + obtainAccessToken();
        MvcResult result = mockMvc.perform(
            get("/news/1")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Traverson traverson = new Traverson(new URI("http://localhost:" + port + linkTo(NewsController.class)
                .slash("1")), MediaTypes.HAL_JSON);
        Traverson.TraversalBuilder traversalBuilder = traverson.follow("self");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        //when
        News news = traversalBuilder.withHeaders(headers).toObject(News.class);

        //then
        assertEquals(1L, news.getId());
    }

    @Test
    void shouldNotGetNewsById() throws Exception {
        String token = "Bearer " + obtainAccessToken();
        MvcResult result = mockMvc.perform(
            get("/news/100")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound())
            .andDo(print())
            .andReturn();
    }

    @SuppressWarnings("unchecked")
    private String obtainAccessToken() throws Exception {
        MvcResult result = mockMvc.perform(
            post("/login")
                    .content("{\"username\":\"test\",\"password\":\"test\"}")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

        HashMap<String, String> mapResult = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(), HashMap.class);

        return mapResult.get("accessToken");
    }
}



