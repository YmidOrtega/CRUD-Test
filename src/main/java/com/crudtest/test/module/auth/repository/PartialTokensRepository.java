package com.crudtest.test.module.auth.repository;

import com.crudtest.test.module.auth.model.PartialTokens;
import com.crudtest.test.module.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PartialTokensRepository extends JpaRepository<PartialTokens, Long> {
    PartialTokens findByToken(String token);

    PartialTokens findByUser(User user);

    void deleteByUser(User user);

    void deleteByToken(String token);

    Optional<PartialTokens> findByUserAndToken(User user, String token);

}
