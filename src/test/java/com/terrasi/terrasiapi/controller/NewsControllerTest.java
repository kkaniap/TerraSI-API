package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.PrepareTest;
import com.terrasi.terrasiapi.Utils.JwtUtils;
import com.terrasi.terrasiapi.model.News;
import com.terrasi.terrasiapi.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestPropertySource(locations = "/application-dev.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsRepository newsRepository;

    @Test
    void shouldGetAllNews() throws Exception {
        //given
        Page<News> news = PrepareTest.getNews();
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(newsRepository.findAll(any(Pageable.class))).willReturn(news);

        Traverson traverson = new Traverson(
                new URI("http://localhost:" + port  + linkTo(NewsController.class)), MediaTypes.HAL_JSON);
        Traverson.TraversalBuilder traversalBuilder = traverson.follow("self");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        //when
        mockMvc.perform(
                get("/news")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        PagedModel<News> resultNews = traversalBuilder.withHeaders(headers).toObject(new ParameterizedTypeReference<>(){});

        //then
        assertEquals(2, resultNews.getContent().size());
    }

    @Test
    void shouldThrow403GetAllNews() throws Exception {
        //then
        mockMvc.perform(
                get("/news")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldThrow404GetAllNews() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(newsRepository.findAll(any(Pageable.class))).willReturn(Page.empty());

        //then
        mockMvc.perform(
                get("/news")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrow406GetAllNews() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");

        //then
        mockMvc.perform(
                get("/news")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    void shouldGetNewsById() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        News news = PrepareTest.getNews().getContent().get(0);
        given(newsRepository.findById(any(Long.class))).willReturn(Optional.of(news));

        String url = "http://localhost:" + port + "/news/1";
        Traverson traverson = new Traverson(
                new URI(url), MediaTypes.HAL_JSON);
        Traverson.TraversalBuilder traversalBuilder = traverson.follow();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        //when
        MvcResult result = mockMvc.perform(
                get("/news/1")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        News resultNews = traversalBuilder.withHeaders(headers).toObject(News.class);

        //them
        assertEquals(1L, resultNews.getId());
    }

    @Test
    void shouldThrow403GetNewsById() throws Exception {
        //then
        mockMvc.perform(
                get("/news/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldThrow404GetNewsById() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(newsRepository.findById(any(Long.class))).willReturn(Optional.empty());

        //then
        mockMvc.perform(
                get("/news/1")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrow406GetNewsById() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");

        //then
        mockMvc.perform(
                get("/news/1")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

}


