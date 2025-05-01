package com.crudtest.test.module.user.dto;

public record UserResponseDTO(
        Long id,
        String plan,
        String email,
        String tokenPartial) {

}
