package com.crudtest.test.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserProfileCompletionDTO(
                @NotNull String uuid,
                @NotBlank String firstName,
                @NotBlank String lastName,
                @NotBlank String username,
                @NotNull LocalDate birthDate,
                @NotBlank String phoneNumber,
                @NotNull AddressDTO address) {
}
