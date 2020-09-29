package com.terrasi.terrasiapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @PastOrPresent
    private LocalDateTime createDate;

    @OneToOne
    private AlertType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Level level;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Level{
        LOW,
        MEDIUM,
        HIGH
    }
}
