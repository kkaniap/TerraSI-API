package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.Utils.JwtUtils;
import com.terrasi.terrasiapi.model.JwtModel;
import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.service.LoginService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody User user){
        Optional<User> loggedUser = loginService.loginUser(user.getUsername(), user.getPassword());
        if(loggedUser.isPresent()){
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", loginService.generateAccessToken(loggedUser.get()));
            tokens.put("refreshToken", loginService.generateRefreshToken(loggedUser.get()));
            return new ResponseEntity<>(tokens, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> refreshToken(@RequestBody Map<String, String> mapToken){
        String username;
        try{
            username = JwtUtils.parseRefreshToken(mapToken.get("refreshToken"));
        }catch (SignatureException | MalformedJwtException e){
            return new ResponseEntity<>("Jwt token not valid",HttpStatus.UNAUTHORIZED);
        }catch (ExpiredJwtException ex){
            return new ResponseEntity<>("Jwt token is expired",HttpStatus.UNAUTHORIZED);
        }
        Optional<String> newToken = loginService.newAccessToken(username);
        return newToken.<ResponseEntity<Object>>map(s -> new ResponseEntity<>(Map.of("accessToken", s), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));
    }
}
