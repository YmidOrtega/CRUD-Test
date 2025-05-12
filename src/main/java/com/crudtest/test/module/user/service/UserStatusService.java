package com.crudtest.test.module.user.service;

import com.crudtest.test.infra.errors.exceptions.UserNotFoundException;
import com.crudtest.test.module.user.dto.*;
import com.crudtest.test.module.user.mapper.UserInformationMapper;
import com.crudtest.test.module.user.model.*;
import com.crudtest.test.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserStatusService {

    private final UserRepository userRepository;
    private final UserInformationMapper userInformationMapper;

    public UserStatusService(
            UserRepository userRepository,
            UserInformationMapper userInformationMapper) {
        this.userRepository = userRepository;
        this.userInformationMapper = userInformationMapper;

    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("El usuario no existe con el uuid: " + id));
    }

    @Transactional
    public UserDeactivatedDTO userDeactivatedDTO(Long id) {
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
