package com.terrasi.terrasiapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor


public class News extends RepresentationModel<News>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonBackReference
    private User user;

    @NotBlank
    private String content;

    @NotNull
    @Min(1)
    private Integer readTime;

    @NotBlank
    private String imgThumbnail;

    @NotBlank
    private String imgThumbnailMobile;

    @NotBlank
    private String imgNews;

    @NotBlank
    private String imgNewsMobile;

    @NotNull
    @PastOrPresent
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createDate;
}
