package com.crudtest.test.module.user.dto;


public record UserDefaultDTO(
        Long id,
        String plan,
        String username,
        String role) {
}
