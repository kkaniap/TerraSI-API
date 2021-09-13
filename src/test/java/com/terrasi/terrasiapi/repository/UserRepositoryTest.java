package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

    UserRepository userRepository;
    UserRoleRepository userRoleRepository;
    TerrariumRepository terrariumRepository;
    AlertTypeRepository alertTypeRepository;
    TestEntityManager entityManager;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository, UserRoleRepository userRoleRepository,
                              TerrariumRepository terrariumRepository, AlertTypeRepository alertTypeRepository,
                              TestEntityManager entityManager) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.terrariumRepository = terrariumRepository;
        this.alertTypeRepository = alertTypeRepository;
        this.entityManager = entityManager;
    }

    private User prepareUser(){
        UserRole role = userRoleRepository.getOne(2L);

        User user = new User();
        user.setUsername("kkarola");
        user.setPassword("n!k1abcD#!");
        user.setFirstName("Karolina");
        user.setLastName("Kania");
        user.setIsMen(false);
        user.setBirthday(LocalDate.of(1997, 4, 10));
        user.setEmail("kkarola@gmail.com");
        user.setPhone("+48568889478");
        user.setAddress("Malinowa 12");
        user.setCity("Katowice");
        user.setState("Slask");
        user.setPostCode("40-091");
        user.setCountry("Poland");
        user.getRoles().add(role);
        user.setIsActive(true);
        user.setAccountCreated(LocalDateTime.of(2019, 12, 20, 10, 2));
        user.getTerrariums().add(prepareTerrarium());
        user.getTerrariums().add(prepareTerrarium());

        return user;
    }

    private Terrarium prepareTerrarium(){
        TerrariumSettings terrariumSettings = new TerrariumSettings();
        terrariumSettings.setLightPower(50);
        terrariumSettings.setHumidityLevel(50);
        terrariumSettings.setSunriseTime(LocalTime.MIDNIGHT);
        terrariumSettings.setSunsetTime(LocalTime.MIDNIGHT);
        terrariumSettings.setSunSpeed(10);
        terrariumSettings.setIsBulbWorking(false);
        terrariumSettings.setAutoManagement(true);

        Alert alert = new Alert();
        alert.setCreateDate(LocalDateTime.now());
        alert.setType(alertTypeRepository.getOne(2L));
        alert.setLevel(Alert.Level.MEDIUM);

        SensorsReads sensorsReads = new SensorsReads();
        sensorsReads.setTemperature(44.5);
        sensorsReads.setHumidity(50);
        sensorsReads.setUvaLevel(400d);
        sensorsReads.setUvbLevel(400d);
        sensorsReads.setWaterLevel(80);
        sensorsReads.setReadDate(LocalDateTime.now());

        Terrarium terrarium = new Terrarium();
        terrarium.setUser(userRepository.getOne(2L));
        terrarium.setName("Spider terrarium");
        terrarium.setTerrariumSettings(terrariumSettings);
        terrarium.setCreateDate(LocalDate.now());
        //terrarium.getSensorsReadsList().add(sensorsReads);
        terrarium.getAlerts().add(alert);

        return terrarium;
    }

    @Test
    public void should_save_user(){
        //given
        User user = prepareUser();

        //then
        assertEquals(entityManager.persistAndFlush(user), userRepository.getOne(user.getId()));
    }

    @Test
    public void should_not_save_user_bad_password(){
        //given
        User toShort = prepareUser();
        toShort.setPassword("K!a2");
        User noDigit = prepareUser();
        noDigit.setPassword("Kan!ia");
        User noLowerCase = prepareUser();
        noLowerCase.setPassword("KAN!IA2");
        User noUpperCase = prepareUser();
        noUpperCase.setPassword("kania!2");
        User noSpecial = prepareUser();
        noSpecial.setPassword("Kania2");

        //then
        assertThrows(ConstraintViolationException.class,() -> entityManager.persistAndFlush(toShort));
        assertThrows(ConstraintViolationException.class,() -> entityManager.persistAndFlush(noDigit));
        assertThrows(ConstraintViolationException.class,() -> entityManager.persistAndFlush(noLowerCase));
        assertThrows(ConstraintViolationException.class,() -> entityManager.persistAndFlush(noUpperCase));
        assertThrows(ConstraintViolationException.class,() -> entityManager.persistAndFlush(noSpecial));
    }

    @Test
    public void should_not_save_user_duplicated_username(){
        //given
        User user = prepareUser();
        User duplicatedUser = prepareUser();

        //when
        entityManager.persistAndFlush(user);

        //then
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(duplicatedUser));
    }

    @Test
    public void should_not_save_user_bad_birthday(){
        //given
        User user = prepareUser();
        user.setBirthday(LocalDate.of(2100, 10, 11));

        //then
        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    public void should_not_save_user_bad_email(){
        //given
        User badDomain = prepareUser();
        badDomain.setEmail("kkaniap@gmail");
        User noDomain = prepareUser();
        noDomain.setEmail("kkaniap@");
        User wrongDomain = prepareUser();
        wrongDomain.setEmail("kkaniap@.com");
        User noAt = prepareUser();
        noAt.setEmail("kkaniapgmail.com");

        //then
        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(badDomain));
        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(noDomain));
        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(wrongDomain));
        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(noAt));
    }

    @Test
    public void should_not_save_user_no_roles(){
        //given
        User user = prepareUser();
        user.getRoles().clear();

        //then
        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    public void should_return_user_news(){
        //given
        List<News> newsList = userRepository.getOne(1L).getNews();

        //then
        assertTrue(newsList.size() > 0);
    }

    @Test
    public void should_return_user_terrariums(){
        //given
        User user = entityManager.persistAndFlush(prepareUser());

        //then
        assertEquals(userRepository.getOne(user.getId()).getTerrariums().size(), 2);
    }

    @Test
    public void should_remove_user_and_terrariums(){
        //given
        User user = entityManager.persistAndFlush(prepareUser());

        //when
        userRepository.delete(user);

        //then
        assertThrows(NoSuchElementException.class, () -> userRepository.findById(user.getId()).get());
        for(Terrarium t : user.getTerrariums()){
            assertThrows(NoSuchElementException.class, () -> terrariumRepository.findById(t.getId()).get());
        }
    }
}
