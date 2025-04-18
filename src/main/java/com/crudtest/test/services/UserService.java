package com.crudtest.test.services;

import com.crudtest.test.dto.*;
import com.crudtest.test.mapper.UserInformationMapper;
import com.crudtest.test.mapper.UserProfileCompletionMapper;
import com.crudtest.test.mapper.UserRegistrationMapper;
import com.crudtest.test.model.Plan;
import com.crudtest.test.model.Role;
import com.crudtest.test.model.User;
import com.crudtest.test.repository.PlanRepository;
import com.crudtest.test.repository.RoleRepository;
import com.crudtest.test.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;

    private final UserInformationMapper userInformationMapper;
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserProfileCompletionMapper userProfileCompletionMapper;



    public UserService(UserRepository userRepository, PlanRepository planRepository, RoleRepository roleRepository, UserInformationMapper userInformationMapper, UserRegistrationMapper userRegistrationMapper, UserProfileCompletionMapper userProfileCompletionMapper) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.roleRepository = roleRepository;
        this.userInformationMapper = userInformationMapper;
        this.userRegistrationMapper = userRegistrationMapper;
        this.userProfileCompletionMapper = userProfileCompletionMapper;
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public User createUser(@Valid UserRegistrationDTO userRegistrationDTO) {
        long planDefaultId = 1L;
        Plan planDefault = planRepository.findById(planDefaultId).orElseThrow(() -> new RuntimeException("Plan not found"));
        long roleDefaultId = 1L;
        Role roleDefault = roleRepository.findById(roleDefaultId).orElseThrow(() -> new RuntimeException("Role not found"));
        User newUser = userRegistrationMapper.toUser(userRegistrationDTO);
        newUser.setCreatedAt(LocalDate.now());
        newUser.setPlanId(planDefault);
        newUser.setRoleId(roleDefault);
        newUser.setActive(true);
        return userRepository.save(newUser);
    }

    public User completeRegistration(@Valid UserProfileCompletionDTO userProfileCompletionDTO) {
        User user = getUserOrThrow(userProfileCompletionDTO.id());
        userProfileCompletionMapper.updateUserFromDTO(userProfileCompletionDTO, user);
        return userRepository.save(user);
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
