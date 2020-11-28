package com.terrasi.terrasiapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrasi.terrasiapi.PrepareTest;
import com.terrasi.terrasiapi.Utils.JwtUtils;
import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.service.LoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/application-dev.properties")
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Test
    void shouldLogin() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        User user = PrepareTest.getUser();
        user.setAccountCreated(null);
        user.setBirthday(null);
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(loginService.loginUser(any(String.class), any(String.class))).willReturn(Optional.of(user));
        given(loginService.generateAccessToken(any(User.class))).willReturn(PrepareTest.getUserAccessToken());

        //when
        MvcResult result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Map<String, String> tokens = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

        //then
        assertNotEquals(null, JwtUtils.parseAccessToken(tokens.get("accessToken")));
    }

    @Test
    void shouldThrow403Login() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        User user = PrepareTest.getUser();
        user.setAccountCreated(null);
        user.setBirthday(null);
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(loginService.loginUser(any(String.class), any(String.class))).willReturn(Optional.empty());

        //then
        MvcResult result = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldRefreshToken() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("refreshToken", PrepareTest.getUserRefreshToken());
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        given(loginService.newAccessToken(any(String.class))).willReturn(Optional.of(PrepareTest.getUserAccessToken()));

        //when
        MvcResult result = mockMvc.perform(
                post("/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(tokens)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String accessToken = mapper.readValue(result.getResponse().getContentAsString(), Map.class).get("accessToken").toString();
        String newToken = accessToken.replace("Bearer ","");
        JwtUtils.parseAccessToken(newToken);

        //then
        assertNotEquals("", newToken);
    }

    @Test
    void shouldThrowSignatureException() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        Map<String, String> tokens = new HashMap<>();
        tokens.put("refreshToken", PrepareTest.getUserRefreshToken() + "Malformed");

        //then
        mockMvc.perform(
                post("/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(tokens)))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }

    @Test
    void shouldThrowExpiredJwtException() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        ReflectionTestUtils.setField(JwtUtils.class, "secretKey", "testSecretKey");
        User user = PrepareTest.getUser();
        long time = System.currentTimeMillis();
        Map<String, String> tokens = new HashMap<>();
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("refresh", true)
                .setIssuedAt(new Date(time))
                .setExpiration(new Date(time - 1))
                .signWith(SignatureAlgorithm.HS512, PrepareTest.SECRET_KEY.getBytes())
                .compact();
        tokens.put("refreshToken", token);

        //then
        mockMvc.perform(
                post("/refreshToken")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(tokens)))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }
}
