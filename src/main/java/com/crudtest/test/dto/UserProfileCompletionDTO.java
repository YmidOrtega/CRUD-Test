package com.crudtest.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserProfileCompletionDTO (
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @NotBlank LocalDate birthDate,
        @NotBlank String phoneNumber,
        @NotNull AddressDTO address
) {
}
