package com.crudtest.test.module.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record PartialUserAuth(
        @NotBlank Long id,
        @NotBlank String email
) {
}
