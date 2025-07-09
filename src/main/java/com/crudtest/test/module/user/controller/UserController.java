package com.crudtest.test.module.user.controller;

import com.crudtest.test.infra.errors.exceptions.TokenAlreadyUsedException;
import com.crudtest.test.infra.errors.exceptions.TokenExpiredException;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.user.dto.AuthUserDTO;
import com.crudtest.test.module.user.dto.*;
import com.crudtest.test.module.user.repository.UserRepository;
import com.crudtest.test.module.user.service.UserRegistrationService;
import com.crudtest.test.module.user.service.UserStatusService;
import com.crudtest.test.module.user.service.UserUpdateService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@SecurityRequirement(name = "bearer-key")
public class UserController {

    private final UserStatusService userStatusService;
    private final UserRegistrationService userRegistrationService;
    private final UserRepository userRepository;
    private final UserUpdateService userUpdateService;

    public UserController(UserStatusService userStatusService, UserRegistrationService userRegistrationService,
            UserRepository userRepository,
            UserUpdateService userUpdateService) {
        this.userStatusService = userStatusService;
        this.userRegistrationService = userRegistrationService;
        this.userRepository = userRepository;
        this.userUpdateService = userUpdateService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDTO> createdUser(@Valid @RequestBody AuthUserDTO authUserDTO,
                                                       UriComponentsBuilder uriBuilder, HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        UserResponseDTO userResponseDTO = userRegistrationService.createUser(authUserDTO, ip, userAgent);

        URI uri = uriBuilder.path("/user/{uuid}").buildAndExpand(userResponseDTO.uuid()).toUri();
        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @PostMapping("/complete-registration")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDefaultDTO> completeRegistration(
            @Valid @RequestBody UserProfileCompletionDTO userProfileCompletionDTO,
            @RequestHeader("Authorization") String partialToken, UriComponentsBuilder uriBuilder)
            throws TokenExpiredException, TokenAlreadyUsedException {

        UserDefaultDTO userDefaultDTO = userRegistrationService.completeRegistration(userProfileCompletionDTO,
                partialToken);
        URI uri = uriBuilder.path("/user/{uuid}").buildAndExpand(userDefaultDTO.uuid()).toUri();
        return ResponseEntity.ok().location(uri).body(userDefaultDTO);
    }

    @GetMapping("/")
    public ResponseEntity<Page<UserDefaultDTO>> newUser(Pageable pagination) {
        Page<UserDefaultDTO> UserDTOPage = userRepository.findByActiveTrue(pagination)
                .map(user -> new UserDefaultDTO(
                        user.getUuid(),
                        user.getPlanId().getName(),
                        user.getUsername(),
                        user.getRoleId().getName()));

        return ResponseEntity.ok(UserDTOPage);
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDefaultDTO> updateUser(@RequestBody UsernameChangeDTO usernameChangeDTO) {
        User user = userUpdateService.updateUsername(usernameChangeDTO);
        UserDefaultDTO userDefaultDTO = new UserDefaultDTO(
                user.getUuid(),
                user.getPlanId().getName(),
                user.getUsername(),
                user.getRoleId().getName());
        return ResponseEntity.ok(userDefaultDTO);
    }


    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        userStatusService.userDeactivatedDTO(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserInformationDTO> userInformation(@PathVariable Long id) {
        UserInformationDTO userInformationDTO = userStatusService.userInformationDTO(id);
        return ResponseEntity.ok(userInformationDTO);
    }
}
