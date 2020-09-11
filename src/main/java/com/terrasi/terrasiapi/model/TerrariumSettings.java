package com.terrasi.terrasiapi.model;

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

    @NotNull
    private LocalTime sunriseTime;

    @NotNull
    private LocalTime sunsetTime;

    @Min(0)
    @NotNull
    private Integer sunSpeed;
}
