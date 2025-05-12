package com.crudtest.test.module.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.crudtest.test.module.user.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenService {

    private static final String ISSUER = "Ymid";
    private static final String SECRET_KEY = "mi-clave-super-secreta-para-firmar-jwt"; // no quiero usar una variable de entorno por ahora

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    // token de usuario
    public String generateToken(User user) {
        validateUser(user);
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(new Date())
                    .withSubject(user.getUuid())
                    .withIssuedAt(new Date())
                    .withExpiresAt(getExpirationTime())
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(ALGORITHM);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error al generar el token", e);
        }
    }

    // verificacion del token si es valido
    public String getSubject(String token) {
        try {
            DecodedJWT jwt = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .acceptLeeway(10)
                    .build()
                    .verify(token);
            return jwt.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token inválido o expirado", e);
        }
    }

    // Token parcial para nuevos usuarios
    public String generatePartialToken(User user) {
        validateUser(user);
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(new Date())
                    .withSubject(user.getUuid())
                    .withIssuedAt(new Date())
                    .withExpiresAt(getExpirationTimePartial())
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(ALGORITHM);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error al generar el token", e);
        }
    }

    private Instant getExpirationTime() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-05:00"));
    }

    public Instant getExpirationTimePartial() {
        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-05:00"));
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getId() == null) {
            throw new IllegalArgumentException("El usuario no tiene email o uuid");
        }
    }

}
