package com.crudtest.test.module.user.service;

import com.crudtest.test.infra.errors.exceptions.InvalidStatusTransitionException;
import com.crudtest.test.infra.errors.exceptions.TokenAlreadyUsedException;
import com.crudtest.test.infra.errors.exceptions.TokenExpiredException;
import com.crudtest.test.infra.errors.exceptions.UserNotFoundException;
import com.crudtest.test.module.auth.model.PartialTokens;
import com.crudtest.test.module.auth.repository.PartialTokensRepository;
import com.crudtest.test.module.auth.service.PartialTokenService;
import com.crudtest.test.module.user.dto.*;
import com.crudtest.test.module.user.mapper.UserInformationMapper;
import com.crudtest.test.module.user.mapper.UserProfileCompletionMapper;
import com.crudtest.test.module.user.mapper.UserRegistrationMapper;
import com.crudtest.test.module.plan.model.Plan;
import com.crudtest.test.module.user.model.*;
import com.crudtest.test.module.plan.repository.PlanRepository;
import com.crudtest.test.module.user.repository.RoleRepository;
import com.crudtest.test.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;
    private final PartialTokensRepository partialTokensRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserInformationMapper userInformationMapper;
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserProfileCompletionMapper userProfileCompletionMapper;
    private final PartialTokenService partialTokenService;



    public UserService(
            UserRepository userRepository,
            PlanRepository planRepository,
            RoleRepository roleRepository,
            PartialTokensRepository partialTokensRepository,
            PasswordEncoder passwordEncoder,
            UserInformationMapper userInformationMapper,
            UserRegistrationMapper userRegistrationMapper,
            UserProfileCompletionMapper userProfileCompletionMapper,
            PartialTokenService partialTokenService
    ) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.roleRepository = roleRepository;
        this.partialTokensRepository = partialTokensRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInformationMapper = userInformationMapper;
        this.userRegistrationMapper = userRegistrationMapper;
        this.userProfileCompletionMapper = userProfileCompletionMapper;
        this.partialTokenService = partialTokenService;;
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("El usuario no existe con el id: " + id));
    }

    @Transactional
    public UserResponseDTO createUser(@Valid AuthUserDTO authUserDTO, String ipAddress, String userAgent) {
        long planDefaultId = 1L;
        Plan planDefault = planRepository.findById(planDefaultId).orElseThrow(() -> new RuntimeException("Plan not found"));
        long roleDefaultId = 1L;
        Role roleDefault = roleRepository.findById(roleDefaultId).orElseThrow(() -> new RuntimeException("Role not found"));


        User newUser = userRegistrationMapper.toUser(authUserDTO);

        newUser.setCreatedAt(LocalDate.now());
        newUser.setPlanId(planDefault);
        newUser.setRoleId(roleDefault);
        newUser.setUuid(UUID.randomUUID().toString());
        newUser.setPassword(passwordEncoder.encode(authUserDTO.password()));
        newUser.setActive(true);
        newUser.setStatus(Status.PENDING);


        userRepository.save(newUser);

        var partialToken = partialTokenService.createPartialToken(newUser, ipAddress, userAgent);

        return new UserResponseDTO(
                newUser.getUuid(),
                newUser.getPlanId().getName(),
                newUser.getEmail(),
                partialToken.getToken()
        );
    }

    @Transactional
    public UserDefaultDTO completeRegistration(@Valid UserProfileCompletionDTO userProfileCompletionDTO, String partialToken) throws TokenExpiredException, TokenAlreadyUsedException {
        User existingUser = getUserOrThrow(userProfileCompletionDTO.id());
        PartialTokens Token = partialTokenService.validateAndConsumeToken(existingUser, partialToken);

        if (!StatusTransitionValidator.canTransition(existingUser.getStatus(), Status.ACTIVE)) {
            throw new InvalidStatusTransitionException("Transición de estado no permitida de " + existingUser.getStatus() + " a " + Status.ACTIVE);
        }
        userProfileCompletionMapper.updateUserFromDTO(userProfileCompletionDTO, existingUser);
        existingUser.setStatus(Status.ACTIVE);
        userRepository.save(existingUser);

        return new UserDefaultDTO(
                existingUser.getId(),
                existingUser.getPlanId().getName(),
                existingUser.getUsername(),
                existingUser.getRoleId().getName());
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
