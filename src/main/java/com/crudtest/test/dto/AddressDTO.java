package com.crudtest.test.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
        @NotBlank
        String streetAddress,
        @NotBlank
        String city,
        @NotBlank
        String state,
        @NotBlank
        String zipCode,
        @NotBlank
        String country
) {
}
