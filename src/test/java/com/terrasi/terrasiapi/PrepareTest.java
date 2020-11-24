package com.terrasi.terrasiapi;

import com.terrasi.terrasiapi.model.News;
import com.terrasi.terrasiapi.model.User;
import com.terrasi.terrasiapi.model.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public final class PrepareTest {

    private static User user;
    private static List<News> news;

    public static User getUser(){
        if(user == null){
            user = new User(1L,"test","test","test","test",true,
                    LocalDate.of(1996,3,2),"test@test.com","test","test","test",
                    "test","test","test", new HashSet<UserRole>(Collections.singletonList(new UserRole(1L, "ROLE_USER"))),
                    true, LocalDateTime.now(), null, null);
        }
        return user;
    }

    public static Page<News> getNews(){
        if(news == null){
            news = new ArrayList<>();
            news.add(new News(1L, getUser(), "test", "test", "test", 5, "test",
                    "test", "test", "test", LocalDateTime.now()));
            news.add(new News(2L, getUser(), "test", "test", "test", 5, "test",
                    "test", "test", "test", LocalDateTime.now()));
        }
        return new PageImpl<>(news);
    }

    public static String getUserAccessToken(){
        if(user == null){
            getUser();
        }
        long time = System.currentTimeMillis();
        String token =  Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles().stream().map(UserRole::getRole).toArray())
                .setIssuedAt(new Date(time))
                .setExpiration(new Date(time + 1000*60*2))
                .signWith(SignatureAlgorithm.HS512, "testSecretKey".getBytes())
                .compact();
        return "Bearer " + token;
    }
}
