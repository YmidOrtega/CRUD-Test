package com.crudtest.test.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan planId;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role roleId;
    private String firstName;
    private String lastName;
    private String username;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate createdAt;
    @Embedded
    private Address address;
    @Column(name = "active")
    private boolean active;

}
