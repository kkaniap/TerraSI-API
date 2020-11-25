package com.terrasi.terrasiapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SensorsReads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double temperature;

    @NotNull
    @Min(0)
    private Integer humidity;

    @NotNull
    @Min(0)
    private Integer brightness;

    @NotNull
    @Min(0)
    private Integer uvaLevel;

    @NotNull
    @Min(0)
    private Integer uvbLevel;

    @NotNull
    @Min(0)
    private Integer waterLevel;

    @NotNull
    @PastOrPresent
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime readDate;
}
