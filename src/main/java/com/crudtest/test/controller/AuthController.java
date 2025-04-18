package com.crudtest.test.controller;

import com.crudtest.test.dto.AuthUserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping ("/login")
    public ResponseEntity <AuthUserDTO> AuthUserString (@Valid AuthUserDTO authUserDTO) {
        Authentication token = new UsernamePasswordAuthenticationToken(
                authUserDTO.email(),
                authUserDTO.password()
        );
        authenticationManager.authenticate(token);
        return ResponseEntity.ok(authUserDTO);

    }
}
