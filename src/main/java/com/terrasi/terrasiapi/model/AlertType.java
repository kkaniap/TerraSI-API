package com.terrasi.terrasiapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AlertType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;
    
    @NotBlank
    private String description;

    public enum Type{
        LOW_WATER_IN_CONTAINER,
        LOW_TEMPERATURE,
        HIGH_TEMPERATURE,
        LOW_UVA_LEVEL,
        LOW_UVB_LEVEL,
        LOW_HUMIDITY_LEVEL,
        HIGH_HUMIDITY_LEVEL,
        TERRARIUM_IS_OPEN;
    }
}
