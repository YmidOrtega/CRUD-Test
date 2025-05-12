package com.crudtest.test.module.user.dto;

public record UserResponseDTO(
        String uuid,
        String plan,
        String email,
        String tokenPartial) {

}
