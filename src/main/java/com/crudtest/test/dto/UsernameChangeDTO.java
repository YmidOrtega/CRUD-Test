package com.crudtest.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsernameChangeDTO(
        @NotBlank Long id,
        @NotBlank @Size(min = 3, max = 15) @Pattern(regexp = "^[a-zA-Z0-9._-]+$") String username) {
}
