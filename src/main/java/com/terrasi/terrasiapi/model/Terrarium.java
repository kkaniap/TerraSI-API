package com.terrasi.terrasiapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Terrarium extends RepresentationModel<Terrarium> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonBackReference
    private User user;

    @NotBlank
    @Size(min = 5)
    private String name;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private TerrariumSettings terrariumSettings;

    @OneToOne(cascade = CascadeType.ALL)
    private SensorsReads sensorsReads;

    @NotNull
    @PastOrPresent
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate createDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TERRARIUM_ID", referencedColumnName = "ID")
    private List<Alert> alerts = new ArrayList<>();
}
