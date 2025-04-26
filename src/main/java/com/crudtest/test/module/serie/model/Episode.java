package com.crudtest.test.module.serie.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "episodes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    private Serie serieId;

    private String name;
    private String description;
    @Column (name = "launch_date")
    private LocalDate launchDate;



    // Getters and Setters
}
