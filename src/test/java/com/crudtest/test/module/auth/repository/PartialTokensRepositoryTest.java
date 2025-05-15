package com.crudtest.test.module.auth.repository;

import com.crudtest.test.module.auth.model.PartialTokens;
import com.crudtest.test.module.plan.model.Plan;
import com.crudtest.test.module.plan.repository.PlanRepository;
import com.crudtest.test.module.user.model.Role;
import com.crudtest.test.module.user.model.Status;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.user.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test") // Cambiado a minúsculas
class PartialTokensRepositoryTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "testPassword123";
    private static final String VALID_TOKEN = "validToken";
    private static final String INVALID_TOKEN = "invalidToken";
    private static final Long DEFAULT_PLAN_ID = 1L;
    private static final Long DEFAULT_ROLE_ID = 1L;

    @Autowired
    private PartialTokensRepository partialTokensRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando el token no coincide")
    void findByUserAndTokenReturnsEmptyWhenTokenDoesNotMatch() {
        // Arrange
        User user = createUser(TEST_EMAIL, TEST_PASSWORD);
        createPartialToken(user, VALID_TOKEN);
        entityManager.flush();

        // Act
        var result = partialTokensRepository.findByUserAndToken(user, INVALID_TOKEN);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando el usuario no existe")
    void findByUserAndTokenReturnsEmptyWhenUserDoesNotExist() {
        // Arrange
        User nonExistentUser = new User();
        nonExistentUser.setId(999L);

        // Act
        var result = partialTokensRepository.findByUserAndToken(nonExistentUser, VALID_TOKEN);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Debería retornar PartialTokens cuando usuario y token coinciden")
    void findByUserAndTokenReturnsPartialTokensWhenMatch() {
        // Arrange
        User user = createUser(TEST_EMAIL, TEST_PASSWORD);
        createPartialToken(user, VALID_TOKEN);
        entityManager.flush();

        // Act
        var result = partialTokensRepository.findByUserAndToken(user, VALID_TOKEN);

        // Assert
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(token -> {
                    assertThat(token.getToken()).isEqualTo(VALID_TOKEN);
                    assertThat(token.getUser()).isEqualTo(user);
                    assertThat(token.isUsed()).isFalse();
                });
    }

    private PartialTokens createPartialToken(User user, String token) {
        PartialTokens partialToken = new PartialTokens();
        partialToken.setUser(user);
        partialToken.setToken(token);
        partialToken.setCreatedAt(LocalDateTime.now());
        partialToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        partialToken.setUsed(false);
        partialToken.setIpAddress("127.0.0.1");
        partialToken.setUserAgent("JUnit Test");
        entityManager.persist(partialToken);
        return partialToken;
    }

    private User createUser(String email, String password) {
        Plan plan = planRepository.findById(DEFAULT_PLAN_ID)
                .orElseThrow(() -> new RuntimeException("Plan por defecto no encontrado"));
        Role role = roleRepository.findById(DEFAULT_ROLE_ID)
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));

        User user = new User();
        user.setRoleId(role);
        user.setPlanId(plan);
        user.setCreatedAt(LocalDate.now());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUuid(UUID.randomUUID().toString());
        user.setActive(true);
        user.setStatus(Status.PENDING);

        entityManager.persist(user);
        return user;
    }
}