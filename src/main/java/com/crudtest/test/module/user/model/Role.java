package com.crudtest.test.module.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role {

    @Id
    private Long id;
    private String name;
    @OneToMany(mappedBy = "roleId", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<User> users;

}
