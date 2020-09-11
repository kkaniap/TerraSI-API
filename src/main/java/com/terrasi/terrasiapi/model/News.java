package com.terrasi.terrasiapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private User user;

    @NotBlank
    private String content;

    @NotEmpty
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
    private LocalDateTime createDate;
}
