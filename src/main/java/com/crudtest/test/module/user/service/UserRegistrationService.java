package com.crudtest.test.module.user.service;

import com.crudtest.test.infra.errors.exceptions.InvalidStatusTransitionException;
import com.crudtest.test.infra.errors.exceptions.TokenAlreadyUsedException;
import com.crudtest.test.infra.errors.exceptions.TokenExpiredException;
import com.crudtest.test.infra.errors.exceptions.UserNotFoundException;
import com.crudtest.test.module.auth.service.PartialTokenService;
import com.crudtest.test.module.plan.model.Plan;
import com.crudtest.test.module.plan.repository.PlanRepository;
import com.crudtest.test.module.user.dto.AuthUserDTO;
import com.crudtest.test.module.user.dto.UserDefaultDTO;
import com.crudtest.test.module.user.dto.UserProfileCompletionDTO;
import com.crudtest.test.module.user.dto.UserResponseDTO;
import com.crudtest.test.module.user.mapper.UserProfileCompletionMapper;
import com.crudtest.test.module.user.mapper.UserRegistrationMapper;
import com.crudtest.test.module.user.model.Role;
import com.crudtest.test.module.user.model.Status;
import com.crudtest.test.module.user.model.StatusTransitionValidator;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.user.repository.RoleRepository;
import com.crudtest.test.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;

    private final PartialTokenService partialTokenService;
    private final PasswordEncoder passwordEncoder;

    private final UserProfileCompletionMapper userProfileCompletionMapper;
    private final UserRegistrationMapper userRegistrationMapper;

    public UserRegistrationService(UserRepository userRepository, PlanRepository planRepository, RoleRepository roleRepository, PartialTokenService partialTokenService, PasswordEncoder passwordEncoder, UserProfileCompletionMapper userProfileCompletionMapper, UserRegistrationMapper userRegistrationMapper) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.roleRepository = roleRepository;
        this.partialTokenService = partialTokenService;
        this.passwordEncoder = passwordEncoder;
        this.userProfileCompletionMapper = userProfileCompletionMapper;
        this.userRegistrationMapper = userRegistrationMapper;
    }

    private User getUserOrThrow(String uuid) {
        return userRepository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException("El usuario no existe con el uuid: " + uuid));
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
                newUser.getUsername(),
                partialToken.getToken()
        );
    }

    @Transactional
    public UserDefaultDTO completeRegistration(@Valid UserProfileCompletionDTO userProfileCompletionDTO, String partialToken) throws TokenExpiredException, TokenAlreadyUsedException {
        User existingUser = getUserOrThrow(userProfileCompletionDTO.uuid());

        if (!StatusTransitionValidator.canTransition(existingUser.getStatus(), Status.ACTIVE)) {
            throw new InvalidStatusTransitionException("Transición de estado no permitida de " + existingUser.getStatus() + " a " + Status.ACTIVE);
        }
        partialTokenService.validateAndConsumeToken(existingUser, partialToken);
        userProfileCompletionMapper.updateUserFromDTO(userProfileCompletionDTO, existingUser);
        existingUser.setStatus(Status.ACTIVE);
        userRepository.save(existingUser);

        return new UserDefaultDTO(
                existingUser.getUuid(),
                existingUser.getPlanId().getName(),
                existingUser.getUsername(),
                existingUser.getRoleId().getName());
    }
}
