package com.crudtest.test.module.serie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String genre;
    @Enumerated(EnumType.STRING)
    private Language language;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "image_url")
    private String imageUrl;
    private boolean active;

    @OneToMany(mappedBy = "serieId", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Episode> episodes;

}
