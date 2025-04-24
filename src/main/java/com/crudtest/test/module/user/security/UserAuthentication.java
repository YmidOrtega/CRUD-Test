package com.crudtest.test.module.user.security;

import com.crudtest.test.module.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthentication implements UserDetailsService {

    private final UserRepository userRepository;

    public UserAuthentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)  throws UsernameNotFoundException {

        return userRepository.findByEmail(email);

    }

}
