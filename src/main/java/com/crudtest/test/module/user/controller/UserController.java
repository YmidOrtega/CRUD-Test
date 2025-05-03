package com.crudtest.test.module.user.controller;

import com.crudtest.test.infra.errors.exceptions.TokenAlreadyUsedException;
import com.crudtest.test.infra.errors.exceptions.TokenExpiredException;
import com.crudtest.test.module.auth.model.PartialTokens;
import com.crudtest.test.module.auth.service.PartialTokenService;
import com.crudtest.test.module.auth.service.TokenService;
import com.crudtest.test.module.user.model.User;
import com.crudtest.test.module.user.dto.AuthUserDTO;
import com.crudtest.test.module.user.dto.*;
import com.crudtest.test.module.user.repository.UserRepository;
import com.crudtest.test.module.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    private final TokenService tokenService;
    private final PartialTokenService partialTokenService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, TokenService tokenService, PartialTokenService partialTokenService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.partialTokenService = partialTokenService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDTO> createdUser(@RequestBody AuthUserDTO authUserDTO, UriComponentsBuilder uriBuilder, HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        UserResponseDTO userResponseDTO = userService.createUser(authUserDTO, ip, userAgent);

        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(userResponseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @PostMapping("/complete-registration")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDefaultDTO> completeRegistration(@RequestBody UserProfileCompletionDTO userProfileCompletionDTO, @RequestHeader("Authorization") String partialToken , UriComponentsBuilder uriBuilder)
            throws TokenExpiredException, TokenAlreadyUsedException {

        UserDefaultDTO userDefaultDTO = userService.completeRegistration(userProfileCompletionDTO, partialToken.toString());
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(userDefaultDTO.id()).toUri();
        return ResponseEntity.ok().location(uri).body(userDefaultDTO);
    }

    @GetMapping("/")
    public ResponseEntity<Page<UserDefaultDTO>> newUser(Pageable pagination) {
        Page<UserDefaultDTO> UserDTOPage = userRepository.findByActiveTrue(pagination)
                .map(user -> new UserDefaultDTO(
                        user.getId(),
                        user.getPlanId().getName(),
                        user.getUsername(),
                        user.getRoleId().getName())
                );

        return ResponseEntity.ok(UserDTOPage);
    }

    @PutMapping ("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDefaultDTO> updateUser(@RequestBody UsernameChangeDTO usernameChangeDTO) {
        User user = userService.updateUsername(usernameChangeDTO);
        UserDefaultDTO userDefaultDTO = new UserDefaultDTO(
                user.getId(),
                user.getPlanId().getName(),
                user.getUsername(),
                user.getRoleId().getName());
        return ResponseEntity.ok(userDefaultDTO);
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        try {
//            userService.deleteUser(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        userService.userDeactivatedDTO(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserInformationDTO> userInformation(@PathVariable Long id) {
        UserInformationDTO userInformationDTO = userService.userInformationDTO(id);
        return ResponseEntity.ok(userInformationDTO);
    }
}
