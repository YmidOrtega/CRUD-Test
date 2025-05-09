package com.crudtest.test.module.user.service;

import com.crudtest.test.infra.errors.exceptions.UserNotFoundException;
import com.crudtest.test.module.user.dto.*;
import com.crudtest.test.module.user.mapper.UserInformationMapper;
import com.crudtest.test.module.user.mapper.UserProfileCompletionMapper;
import com.crudtest.test.module.user.model.*;
import com.crudtest.test.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserInformationMapper userInformationMapper;

    private final UserProfileCompletionMapper userProfileCompletionMapper;




    public UserService(
            UserRepository userRepository,
            UserInformationMapper userInformationMapper,
            UserProfileCompletionMapper userProfileCompletionMapper) {
        this.userRepository = userRepository;
        this.userInformationMapper = userInformationMapper;
        this.userProfileCompletionMapper = userProfileCompletionMapper;

    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("El usuario no existe con el id: " + id));
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

//    @Transactional
//    public void deleteUser(Long id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        userRepository.delete(user);
//    }

    @Transactional
    public UserDeactivatedDTO userDeactivatedDTO (Long id) {
        User user = getUserOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
        return new UserDeactivatedDTO();
    }

    public UserInformationDTO userInformationDTO(Long id) {
        User user = getUserOrThrow(id);
        if (!user.isActive()) {
            return null;
        }
        return userInformationMapper.toUserInformationDTO(user);
    }
}
