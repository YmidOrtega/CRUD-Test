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
@ActiveProfiles("test")
class PartialTokensRepositoryTest {

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
    @DisplayName("Returns empty Optional when token does not match for user")
    void findByUserAndTokenReturnsEmptyWhenTokenDoesNotMatch() {
        User user = createUser("ymid@gmail.com", "password");
        createPartialToken(user, "validToken");
        var partialTokens = partialTokensRepository.findByUserAndToken(user, "invalidToken");
        assertThat(partialTokens).isEmpty();

    }

    @Test
    @DisplayName("Returns empty Optional when user does not exist")
    void findByUserAndTokenReturnsEmptyWhenUserDoesNotExist() {
        User nonExistentUser = new User();
        nonExistentUser.setId(999L);
        var partialTokens = partialTokensRepository.findByUserAndToken(nonExistentUser, "token");
        assertThat(partialTokens).isEmpty();
    }

    @Test
    @DisplayName("Returns Optional with PartialTokens when user and token match")
    void findByUserAndTokenReturnsPartialTokensWhenUserAndTokenMatch() {
        User user = createUser("ymid@gmail.com", "password");
        createPartialToken(user, "validToken");
        var result = partialTokensRepository.findByUserAndToken(user, "validToken");
        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo("validToken");
    }

    private PartialTokens createPartialToken(User user, String token) {
        PartialTokens partialToken = new PartialTokens();
        partialToken.setUser(user);
        partialToken.setToken(token);
        partialToken.setCreatedAt(LocalDateTime.now());
        LocalDateTime localExpiration = LocalDateTime.now().plusDays(1);
        partialToken.setExpiresAt(localExpiration);
        partialToken.setUsed(false);
        partialToken.setIpAddress("0:0:0:0:0:0:0:1");
        partialToken.setUserAgent("PostmanRuntime/7.43.4");
        entityManager.persist(partialToken);
        return partialToken;
    }

    private User createUser(String email, String password) {

        long planDefaultId = 1L;
        Plan plan = planRepository.findById(planDefaultId).orElseThrow(() -> new RuntimeException("Plan not found"));
        long roleDefaultId = 1L;
        Role role = roleRepository.findById(roleDefaultId).orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setRoleId(role);
        user.setPlanId(plan);
        user.setCreatedAt(LocalDate.now());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUuid(UUID.randomUUID().toString());
        user.setActive(true);
        user.setStatus(Status.PENDING);

        user.setEmail(email);
        user.setPassword(password);
        entityManager.persist(user);
        return user;
    }

}