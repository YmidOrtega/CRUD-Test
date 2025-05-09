package com.crudtest.test.module.auth.service;

import com.crudtest.test.infra.errors.exceptions.InvalidTokenException;
import com.crudtest.test.infra.errors.exceptions.TokenAlreadyUsedException;
import com.crudtest.test.infra.errors.exceptions.TokenExpiredException;
import com.crudtest.test.module.auth.model.PartialTokens;
import com.crudtest.test.module.auth.repository.PartialTokensRepository;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.user.repository.UserRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@EnableScheduling
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
    public PartialTokens validateAndConsumeToken(User user, String token) throws TokenAlreadyUsedException, TokenExpiredException {
        String tokenNow = token.replace("Bearer ", "");
        String email = tokenService.getSubject(tokenNow);
        if (!user.getEmail().equals(email)) {
            throw new InvalidTokenException("El token no corresponde al usuario actual");
        }
        PartialTokens partialToken = partialTokensRepository.findByUserAndToken(user, tokenNow).orElseThrow(() -> new InvalidTokenException("Token inválido o no encontrado"));

        if (partialToken.isUsed()) throw new TokenAlreadyUsedException("Este token ya fue usado");
        if (partialToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            partialTokensRepository.delete(partialToken);
            throw new TokenExpiredException("El token ha expirado") ;
        }

        try {
            partialToken.setUsed(true);
            return partialTokensRepository.save(partialToken);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TokenAlreadyUsedException("Este token ya fue usado por otra solicitud concurrente");
        }
    }
    @Transactional
    @Scheduled(cron = "0 0 0 1 * ?")
    public void cleanUpTokens () {
        LocalDateTime now = LocalDateTime.now();
        partialTokensRepository.deleteAllByUsedTrueOrExpiresAtBefore(now);
    }

}
