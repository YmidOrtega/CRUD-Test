package com.crudtest.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    private String description;
    @Positive
    private Double price;
    private String benefits;
    @Column (name = "screen_limit")
    @Positive
    private int screenLimit;

    @OneToMany(mappedBy = "planId", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<User> users;

}
