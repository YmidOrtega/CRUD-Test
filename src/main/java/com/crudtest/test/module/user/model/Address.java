package com.crudtest.test.module.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class Address {

    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;

}

