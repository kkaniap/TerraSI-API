package com.terrasi.terrasiapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Terrarium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private User user;

    @NotBlank
    @Size(min = 5)
    private String name;

    @NotNull
    private Boolean autoManagement;

    @NotNull
    @OneToOne
    private TerrariumSettings terrariumSettings;

    @OneToMany
    @JoinColumn(name = "TERRARIUM_ID", referencedColumnName = "ID")
    private List<SensorsReads> sensorsReadsList = new ArrayList<>();

    @NotNull
    @PastOrPresent
    private LocalDate createDate;

    @OneToMany
    @JoinColumn(name = "TERRARIUM_ID", referencedColumnName = "ID")
    private List<Alert> alerts = new ArrayList<>();
}
