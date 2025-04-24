package com.crudtest.test.module.user.dto;

import java.time.LocalDate;

public record UserInformationDTO(
        String plan,
        String role,
        String email,
        String firstName,
        String lastName,
        String username,
        LocalDate birthDate,
        String phoneNumber
) {
}
