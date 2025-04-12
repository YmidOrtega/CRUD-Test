package com.crudtest.test.dto;

import jakarta.validation.constraints.NotNull;

public record UsernameChangeDTO(@NotNull Long id, @NotNull String username) {

}
