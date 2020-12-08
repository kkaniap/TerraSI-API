package com.terrasi.terrasiapi;

import com.terrasi.terrasiapi.model.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public final class PrepareTest {

    public static final String SECRET_KEY = "testSecretKey";

    private static User user;
    private static TerrariumSettings terrariumSettings;
    private static List<News> news;
    private static List<Alert> alerts;
    private static List<Terrarium> terrariums;
    private static List<SensorsReads> sensorsReads;

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

    public static Page<Terrarium> getTerrariums(){
        if(terrariums == null){
            terrariums = new ArrayList<>();
            terrariums.add(new Terrarium(1L, getUser(), "test", "localhost:8081", getTerrariumSettings(), getSensorsReads(),
                    LocalDate.now(), getAlerts()));
            terrariums.add(new Terrarium(2L, getUser(), "test", "localhost:8081", getTerrariumSettings(), getSensorsReads(),
                    LocalDate.now(), getAlerts()));
        }
        return new PageImpl<>(terrariums);
    }

    public static TerrariumSettings getTerrariumSettings(){
        if(terrariumSettings == null){
            terrariumSettings = new TerrariumSettings(1L, 50, 50, LocalTime.now(),
                    LocalTime.now(), 50, false, true);
        }
        return terrariumSettings;
    }

    public static List<SensorsReads> getSensorsReads(){
        if(sensorsReads == null){
            sensorsReads = new ArrayList<>();
            sensorsReads.add(new SensorsReads(1L, 60.5, 50, 50, 50, 50,
                    50, LocalDateTime.now()));
            sensorsReads.add(new SensorsReads(2L, 60.5, 50, 50, 50, 50,
                    50, LocalDateTime.now()));
        }
        return sensorsReads;
    }

    public static List<Alert> getAlerts(){
        if(alerts == null){
            alerts = new ArrayList<>();
            AlertType alertType = new AlertType(1L, AlertType.Type.HIGH_TEMPERATURE, "test");
            alerts.add(new Alert(1L, LocalDateTime.now(), alertType, Alert.Level.HIGH));
            alerts.add(new Alert(2L, LocalDateTime.now(), alertType, Alert.Level.HIGH));
        }
        return alerts;
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
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
        return "Bearer " + token;
    }

    public static String getUserRefreshToken(){
        if(user == null){
            getUser();
        }
        long time = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("refresh", true)
                .setIssuedAt(new Date(time))
                .setExpiration(new Date(time + 1000*60*60*24*7))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }
}
