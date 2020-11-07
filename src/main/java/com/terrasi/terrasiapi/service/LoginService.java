package com.terrasi.terrasiapi.service;

import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.model.UserRole;
import com.terrasi.terrasiapi.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final String secretKey;

    public LoginService(UserRepository userRepository,@Value("${secret.key}") String secretKey) {
        this.userRepository = userRepository;
        this.secretKey = secretKey;
    }

    public Optional<User> loginUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username,password);
    }

    public String generateAccessToken(User user){
        long time = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles().stream().map(UserRole::getRole).toArray())
                .setIssuedAt(new Date(time))
                .setExpiration(new Date(time + 1000*15))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public String generateRefreshToken(User user){
        long time = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("refresh", true)
                .setIssuedAt(new Date(time))
                .setExpiration(new Date(time + 1000*60*60*24*7))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public Optional<String> newAccessToken(String username){
        Optional<User> user = userRepository.findByUsername(username);
        Optional<String> newToken = Optional.empty();
        if(user.isPresent()){
            newToken = Optional.of(generateAccessToken(user.get()));
        }
        return newToken;
    }
}
