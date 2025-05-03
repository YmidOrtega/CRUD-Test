package com.crudtest.test.module.user.dto;

public record UserResponseDTO(
        String id,
        String plan,
        String email,
        String tokenPartial) {

}
