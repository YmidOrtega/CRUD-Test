package com.crudtest.test.module.user.dto;

import com.crudtest.test.module.user.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserProfileCompletionDTO (
        @NotNull Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @NotBlank LocalDate birthDate,
        @NotBlank String phoneNumber,
        @NotNull AddressDTO address
) {
}
