//package com.terrasi.terrasiapi.controller;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.terrasi.terrasiapi.model.News;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.hateoas.MediaTypes;
//import org.springframework.hateoas.client.Traverson;
//import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class NewsControllerTest {
//
//    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//
//    @Autowired
//    MockMvc mockMvc;
//
//
//    @Test
//    void shouldGetAllNews() throws Exception {
//        Traverson traverson = new Traverson(new URI("/news"), MediaTypes.HAL_JSON);
//
//
//        //given
//        MvcResult result = mockMvc.perform(
//                get("/news")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//        //when
//
//        List<News> news = traverson.follow().toObject(new ParameterizedTypeReference<>() {
//        });
//        //then
////        assertEquals(3, news.size());
//    }
//
//}
//
//
//
