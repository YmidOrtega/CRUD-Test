package com.crudtest.test.infra.security;

import com.crudtest.test.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthentication implements UserDetailsService {

    private final UserRepository userRepository;

    public UserAuthentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        return userRepository.findByEmail(email);

    }

}
