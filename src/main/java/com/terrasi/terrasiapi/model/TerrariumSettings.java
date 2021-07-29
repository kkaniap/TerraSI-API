package com.terrasi.terrasiapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TerrariumSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @Max(100)
    @NotNull
    private Integer lightPower;

    @Min(0)
    @Max(100)
    @NotNull
    private Integer humidityLevel;

    @Min(0)
    @Max(100)
    @NotNull
    private Integer waterLevel;

    @NotNull
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime sunriseTime;

    @NotNull
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime sunsetTime;

    @Min(0)
    @NotNull
    private Integer sunSpeed;

    @NotNull
    private Boolean isBulbWorking;

    @NotNull
    private Boolean isHumidifierWorking;

    @NotNull
    private Boolean autoManagement;
}
