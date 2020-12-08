package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.Alert;
import com.terrasi.terrasiapi.model.SensorsReads;
import com.terrasi.terrasiapi.model.Terrarium;
import com.terrasi.terrasiapi.model.TerrariumSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class TerrariumRepositoryTest {

    TestEntityManager entityManager;
    TerrariumRepository terrariumRepository;
    UserRepository userRepository;
    AlertTypeRepository alertTypeRepository;
    TerrariumSettingsRepository settingsRepository;
    SensorsReadsRepository readsRepository;
    AlertRepository alertRepository;

    @Autowired
    public TerrariumRepositoryTest(TestEntityManager entityManager, TerrariumRepository terrariumRepository,
                                   UserRepository userRepository, AlertTypeRepository alertTypeRepository,
                                   TerrariumSettingsRepository settingsRepository,
                                   SensorsReadsRepository readsRepository, AlertRepository alertRepository) {
        this.entityManager = entityManager;
        this.terrariumRepository = terrariumRepository;
        this.userRepository = userRepository;
        this.alertTypeRepository = alertTypeRepository;
        this.settingsRepository = settingsRepository;
        this.readsRepository = readsRepository;
        this.alertRepository = alertRepository;
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
        sensorsReads.setBrightness(100);
        sensorsReads.setUvaLevel(400);
        sensorsReads.setUvbLevel(400);
        sensorsReads.setWaterLevel(80);
        sensorsReads.setReadDate(LocalDateTime.now());

        Terrarium terrarium = new Terrarium();
        terrarium.setUser(userRepository.getOne(2L));
        terrarium.setName("Spider terrarium");
        terrarium.setTerrariumSettings(terrariumSettings);
        terrarium.setCreateDate(LocalDate.now());
        terrarium.getSensorsReadsList().add(sensorsReads);
        terrarium.getAlerts().add(alert);
        terrarium.setIp("localhost:8081");

        return terrarium;
    }

    @Test
    public void should_save_terrarium(){
        //given
        Terrarium terrarium = prepareTerrarium();

        //then
        assertEquals(entityManager.persistAndFlush(terrarium), terrariumRepository.getOne(terrarium.getId()));
    }

    @Test
    public void should_return_owner(){
        //given
        Terrarium terrarium = entityManager.persistAndFlush(prepareTerrarium());

        //then
        assertEquals(terrarium.getUser(),
                terrariumRepository.getOne(terrarium.getId()).getUser());
    }

    @Test
    public void should_return_terrarium_settings(){
        //given
        Terrarium terrarium = entityManager.persistAndFlush(prepareTerrarium());

        //then
        assertEquals(terrarium.getTerrariumSettings(),
                terrariumRepository.getOne(terrarium.getId()).getTerrariumSettings());
    }

    @Test
    public void should_return_sensor_reads_list(){
        //given
        Terrarium terrarium = entityManager.persistAndFlush(prepareTerrarium());

        //then
        assertEquals(terrarium.getSensorsReadsList().size(),
                terrariumRepository.getOne(terrarium.getId()).getSensorsReadsList().size());
    }

    @Test
    public void should_return_alerts_list(){
        //given
        Terrarium terrarium = entityManager.persistAndFlush(prepareTerrarium());

        //then
        assertEquals(terrarium.getAlerts().size(),
                terrariumRepository.getOne(terrarium.getId()).getAlerts().size());
    }

    @Test
    public void should_remove_terrarium_settings_reads_alerts(){
        //given
        Terrarium terrarium = entityManager.persistAndFlush(prepareTerrarium());

        //when
        terrariumRepository.delete(terrarium);

        //then
        assertThrows(NoSuchElementException.class, () -> terrariumRepository.findById(terrarium.getId()).get());
        assertThrows(NoSuchElementException.class,
                () -> settingsRepository.findById(terrarium.getTerrariumSettings().getId()).get());
        for(SensorsReads sr : terrarium.getSensorsReadsList()){
            assertThrows(NoSuchElementException.class,
                    () -> readsRepository.findById(sr.getId()).get());
        }
        for (Alert a : terrarium.getAlerts()){
            assertThrows(NoSuchElementException.class,
                    () -> alertRepository.findById(a.getId()).get());
        }
    }
}
