package com.crudtest.test.repository;

import com.crudtest.test.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository <User, Long> {
    boolean existsByUsername(@NotBlank String username);

    Page<User> findByActiveTrue(Pageable pagination);

    UserDetails findByEmail(String email);
}
