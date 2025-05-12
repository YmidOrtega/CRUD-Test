package com.crudtest.test.module.user.service;

import com.crudtest.test.infra.errors.exceptions.UserNotFoundException;
import com.crudtest.test.module.user.dto.UsernameChangeDTO;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateService {

    private final UserRepository userRepository;

    public UserUpdateService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("El usuario no existe con el uuid: " + id));
    }

    @Transactional
    public User updateUsername(@Valid UsernameChangeDTO usernameChangeDTO) {
        User user = getUserOrThrow(usernameChangeDTO.id());
        if (user.getUsername().equals(usernameChangeDTO.username())) {
            throw new IllegalArgumentException("The new username is the same as the current one");
        }
        if (userRepository.existsByUsername(usernameChangeDTO.username())) {
            throw new IllegalArgumentException("The username already exists");
        }
        user.setUsername(usernameChangeDTO.username());
        return userRepository.save(user);
    }
}
