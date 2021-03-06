package com.terrasi.terrasiapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class News extends RepresentationModel<News>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonBackReference
    private User user;

    @NotBlank
    private String shortContent;

    private String content;

    @NotBlank
    private String title;

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

    @JsonProperty("createUser")
    private String jsonUserName(){
        return user.getFirstName() + " " + user.getLastName();
    }
}
