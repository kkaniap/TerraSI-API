package com.terrasi.terrasiapi.controller;

import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.model.UserRole;
import com.terrasi.terrasiapi.service.LoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/login")
public class LoginController {

    private final LoginService loginService;
    private final String secretKey;

    public LoginController(LoginService loginService,@Value("${secret.key}") String secretKey) {
        this.loginService = loginService;
        this.secretKey = secretKey;
    }

    @PostMapping
    public String login(@RequestBody User user){
        User loggedUser = loginService.loginUser(user.getUsername(), user.getPassword());
        if(loggedUser != null){
            long time = System.currentTimeMillis();
            return Jwts.builder()
                    .setSubject(loggedUser.getUsername())
                    .claim("roles", loggedUser.getRoles().stream().map(UserRole::getRole).toArray())
                    .setIssuedAt(new Date(time))
                    .setExpiration(new Date(time + 1000*60*60))
                    .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                    .compact();
        }
        return null;
    }
}
