package com.crudtest.test.module.auth.service;

import com.crudtest.test.infra.errors.exceptions.InvalidTokenException;
import com.crudtest.test.infra.errors.exceptions.TokenAlreadyUsedException;
import com.crudtest.test.infra.errors.exceptions.TokenExpiredException;
import com.crudtest.test.module.auth.model.PartialTokens;
import com.crudtest.test.module.auth.repository.PartialTokensRepository;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.user.repository.UserRepository;
import com.crudtest.test.module.user.service.UserService;

import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class PartialTokenService
{
    private final PartialTokensRepository partialTokensRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public PartialTokenService(PartialTokensRepository partialTokensRepository, UserRepository userRepository, TokenService tokenService) {
        this.partialTokensRepository = partialTokensRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    // Method to create a partial token
    public PartialTokens createPartialToken(User newUser, String ipAddress, String userAgent) {
        PartialTokens partialToken = new PartialTokens();
        partialToken.setUser(newUser);

        String partialTokenString = tokenService.generatePartialToken(newUser);
        LocalDateTime localExpiration = LocalDateTime.ofInstant(tokenService.getExpirationTimePartial(), ZoneId.of("-05:00"));

        partialToken.setToken(partialTokenString);
        partialToken.setCreatedAt(LocalDateTime.now());
        partialToken.setExpiresAt(localExpiration);
        partialToken.setUsed(false);
        partialToken.setIpAddress(ipAddress);
        partialToken.setUserAgent(userAgent);
        return partialTokensRepository.save(partialToken);
    }

    // Method to find a partial token by its token string
    public PartialTokens validateAndConsumeToken(User user, PartialTokens token) throws TokenAlreadyUsedException, TokenExpiredException {
        PartialTokens partialToken = partialTokensRepository.findByTokenAndUser(user, token);
                //.orElseThrow(() -> new InvalidTokenException("Token inválido o no encontrado"));

        if (partialToken.isUsed()) throw new TokenAlreadyUsedException("Este token ya fue usado");
        if (partialToken.getExpiresAt().isBefore(LocalDateTime.now())) throw new TokenExpiredException("El token ha expirado");

        partialToken.setUsed(true);
        return partialTokensRepository.save(partialToken);
    }

}
