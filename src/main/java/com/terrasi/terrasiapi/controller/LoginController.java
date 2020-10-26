package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.Utils.JwtUtils;
import com.terrasi.terrasiapi.model.JwtModel;
import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.service.LoginService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
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
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken){
        JwtModel jwtModel;
        try{
            jwtModel = JwtUtils.parseJWT(refreshToken);
        }catch (SignatureException ex){
            return new ResponseEntity<>("Jwt token not valid",HttpStatus.UNAUTHORIZED);
        }catch (ExpiredJwtException ex){
            return new ResponseEntity<>("Jwt token is expired",HttpStatus.UNAUTHORIZED);
        }
        Optional<String> newToken = loginService.newAccessToken(jwtModel.getUsername());
        return newToken.map(s -> new ResponseEntity<>(s, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("Bad credentials", HttpStatus.UNAUTHORIZED));
    }
}
