package com.crudtest.test.module.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthUserDTO(
        @NotBlank @Email String email,
        @NotBlank String password
        ) {}
