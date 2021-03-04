package com.terrasi.terrasiapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrasi.terrasiapi.PrepareTest;
import com.terrasi.terrasiapi.Utils.JwtUtils;
import com.terrasi.terrasiapi.exception.ForbiddenException;
import com.terrasi.terrasiapi.exception.NotFoundException;
import com.terrasi.terrasiapi.exception.UnauthorizedException;
import com.terrasi.terrasiapi.model.Terrarium;
import com.terrasi.terrasiapi.model.TerrariumSettings;
import com.terrasi.terrasiapi.service.TerrariumService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestPropertySource(locations = "/application-dev.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TerrariumControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TerrariumService terrariumService;

    @Test
    void shouldGetAllTerrariumsForUser() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        Page<Terrarium> terrariums = PrepareTest.getTerrariums();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.getTerrariumsByToken(any(String.class), any(Pageable.class))).willReturn(terrariums);

        Traverson traverson = new Traverson(
                new URI("http://localhost:" + port + linkTo(TerrariumController.class)), MediaTypes.HAL_JSON);
        Traverson.TraversalBuilder traversalBuilder = traverson.follow("self");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        //when
        MvcResult result = mockMvc.perform(
                get("/terrariums")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        PagedModel<Terrarium> resultTerrariums = traversalBuilder.withHeaders(headers).toObject(new ParameterizedTypeReference<>(){});

        //then
        assertEquals(2, terrariums.getContent().size());
    }

    @Test
    void shouldThrowGetAllTerrariumsForUser() throws Exception {
        //

        mockMvc.perform(
                get("/terrariums")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldThrow404GetAllTerrariumsForUser() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.getTerrariumsByToken(any(String.class), any(Pageable.class))).willReturn(Page.empty());

        //then
        mockMvc.perform(
                get("/terrariums")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrow406GetAllTerrariumsForUser() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");

        //then
        mockMvc.perform(
                get("/terrariums")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    void shouldGetTerrariumById() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        Page<Terrarium> terrariums = PrepareTest.getTerrariums();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.getTerrariumById(any(Long.class), any(String.class))).willReturn(terrariums.toList().get(0));

        Traverson traverson = new Traverson(new URI("http://localhost:" + port + linkTo(TerrariumController.class)
                .slash("1")), MediaTypes.HAL_JSON);
        Traverson.TraversalBuilder traversalBuilder = traverson.follow();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        //when
        MvcResult result = mockMvc.perform(
                get("/terrariums/1")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Terrarium terrarium = traversalBuilder.withHeaders(headers).toObject(Terrarium.class);

        //then
        assertEquals(1L, terrarium.getId());
    }

    @Test
    void shouldThrow401GetTerrariumById() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.getTerrariumById(any(Long.class), any(String.class))).willThrow(new UnauthorizedException());

        MvcResult result = mockMvc.perform(
                get("/terrariums/1")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldThrow403GetTerrariumById() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.getTerrariumById(any(Long.class), any(String.class))).willThrow(new ForbiddenException());

        //then
        MvcResult result = mockMvc.perform(
                get("/terrariums/1")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldThrow404GetTerrariumById() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.getTerrariumById(any(Long.class), any(String.class))).willThrow(new NotFoundException());

        //then
        mockMvc.perform(
                get("/terrariums/1")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldThrow406GetTerrariumById() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");

        //then
        mockMvc.perform(
                get("/terrariums/1")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    void shouldSaveTerrariumSettings() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        TerrariumSettings settings = PrepareTest.getTerrariumSettings();
        settings.setSunriseTime(null);
        settings.setSunsetTime(null);
        given(terrariumService.saveTerrariumSettings(any(Long.class), any(String.class), any(TerrariumSettings.class))).willReturn(true);

        //then
        MvcResult result = mockMvc.perform(
                put("/terrariums/1/settings")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldThrow401SaveTerrariumSettings() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        TerrariumSettings settings = PrepareTest.getTerrariumSettings();
        settings.setSunriseTime(null);
        settings.setSunsetTime(null);
        given(terrariumService.saveTerrariumSettings(any(Long.class), any(String.class), any(TerrariumSettings.class))).willThrow(new UnauthorizedException());

        //then
        MvcResult result = mockMvc.perform(
                put("/terrariums/1/settings")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldThrow403SaveTerrariumSettings() throws Exception {
        //given
        String token = PrepareTest.getUserAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        TerrariumSettings settings = PrepareTest.getTerrariumSettings();
        settings.setSunriseTime(null);
        settings.setSunsetTime(null);
        given(terrariumService.saveTerrariumSettings(any(Long.class), any(String.class), any(TerrariumSettings.class))).willThrow(new ForbiddenException());

        //then
        MvcResult result = mockMvc.perform(
                put("/terrariums/1/settings")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldThrow404SaveTerrariumSettings() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        TerrariumSettings settings = PrepareTest.getTerrariumSettings();
        settings.setSunriseTime(null);
        settings.setSunsetTime(null);
        given(terrariumService.saveTerrariumSettings(any(Long.class), any(String.class), any(TerrariumSettings.class))).willThrow(new NotFoundException());

        //then
        MvcResult result = mockMvc.perform(
                put("/terrariums/1/settings")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldUpdateTerrariumName() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.updateTerrariumName(any(Long.class), any(String.class), any(String.class))).willReturn(true);

        //then
        MvcResult result = mockMvc.perform(
                patch("/terrariums/1/name")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("newName"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldThrow401UpdateTerrariumName() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.updateTerrariumName(any(Long.class), any(String.class), any(String.class))).willThrow(UnauthorizedException.class);

        //then
        MvcResult result = mockMvc.perform(
                patch("/terrariums/1/name")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("newName"))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldThrow404UpdateTerrariumName() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        String token = PrepareTest.getUserAccessToken();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(terrariumService.updateTerrariumName(any(Long.class), any(String.class), any(String.class))).willThrow(NotFoundException.class);

        //then
        MvcResult result = mockMvc.perform(
                patch("/terrariums/1/name")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("newName"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }
}
