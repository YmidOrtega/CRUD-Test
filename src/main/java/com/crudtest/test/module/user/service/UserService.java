package com.crudtest.test.module.user.service;

import com.crudtest.test.module.user.mapper.UserInformationMapper;
import com.crudtest.test.module.user.mapper.UserProfileCompletionMapper;
import com.crudtest.test.module.user.mapper.UserRegistrationMapper;
import com.crudtest.test.module.plan.model.Plan;
import com.crudtest.test.module.user.model.Role;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.auth.dto.AuthUserDTO;
import com.crudtest.test.module.user.dto.UserDeactivatedDTO;
import com.crudtest.test.module.user.dto.UserInformationDTO;
import com.crudtest.test.module.user.dto.UserProfileCompletionDTO;
import com.crudtest.test.module.user.dto.UsernameChangeDTO;
import com.crudtest.test.module.plan.repository.PlanRepository;
import com.crudtest.test.module.user.repository.RoleRepository;
import com.crudtest.test.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserInformationMapper userInformationMapper;
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserProfileCompletionMapper userProfileCompletionMapper;



    public UserService(UserRepository userRepository, PlanRepository planRepository, RoleRepository roleRepository, UserInformationMapper userInformationMapper, UserRegistrationMapper userRegistrationMapper, UserProfileCompletionMapper userProfileCompletionMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.roleRepository = roleRepository;
        this.userInformationMapper = userInformationMapper;
        this.userRegistrationMapper = userRegistrationMapper;
        this.userProfileCompletionMapper = userProfileCompletionMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public User createUser(@Valid AuthUserDTO authUserDTO) {
        long planDefaultId = 1L;
        Plan planDefault = planRepository.findById(planDefaultId).orElseThrow(() -> new RuntimeException("Plan not found"));
        long roleDefaultId = 1L;
        Role roleDefault = roleRepository.findById(roleDefaultId).orElseThrow(() -> new RuntimeException("Role not found"));
        User newUser = userRegistrationMapper.toUser(authUserDTO);
        newUser.setCreatedAt(LocalDate.now());
        newUser.setPlanId(planDefault);
        newUser.setRoleId(roleDefault);
        newUser.setPassword(passwordEncoder.encode(authUserDTO.password()));
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
