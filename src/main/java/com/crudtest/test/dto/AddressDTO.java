package com.crudtest.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        @NotBlank String streetAddress,
        @NotBlank String city,
        @NotBlank @Size(min = 2, max = 50) @Pattern(regexp = "^[a-zA-Z ]+$") String state,
        @NotBlank @Size(min = 5, max = 10) @Pattern(regexp = "^[0-9]+$") String zipCode,
        @NotBlank @Size(min = 2, max = 50) @Pattern(regexp = "^[a-zA-Z ]+$") String country
) {
}
