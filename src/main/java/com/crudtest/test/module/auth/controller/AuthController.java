package com.crudtest.test.module.auth.controller;

import com.crudtest.test.module.user.dto.AuthUserDTO;
import com.crudtest.test.module.auth.dto.JwtTokenDTO;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping ("/login")
    public ResponseEntity<JwtTokenDTO> AuthUser (@RequestBody @Valid AuthUserDTO authUserDTO) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                authUserDTO.email(),
                authUserDTO.password()
        );
        var UserAuth = authenticationManager.authenticate(authToken);
        var jwtToken = tokenService.generateToken((User) UserAuth.getPrincipal());
        return ResponseEntity.ok(new JwtTokenDTO(jwtToken));

    }

}
