package com.crudtest.test.dto;

import java.time.LocalDate;

public record UserInformationDTO(
        String plan,
        String email,
        String firstName,
        String lastName,
        String username,
        LocalDate birthDate,
        String phoneNumber
) {
}
