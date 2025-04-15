package com.crudtest.test.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserProfileCompletionDTO (
        @NotBlank Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Size(min = 3, max = 15) @Pattern(regexp = "^[a-zA-Z0-9._-]+$") String username,
        @NotBlank @Past LocalDate birthDate,
        @NotBlank @Size(min = 10, max = 15) @Pattern(regexp = "^[0-9]+$") String phoneNumber,
        @NotNull AddressDTO address
) {
}
