package com.crudtest.test.controller;

import com.crudtest.test.dto.*;
import com.crudtest.test.model.User;
import com.crudtest.test.repository.UserRepository;
import com.crudtest.test.services.UserService;
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

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDTO> createdUser(@RequestBody UserRegistrationDTO userRegistrationDTO, UriComponentsBuilder uriBuilder) {
        User createdUser = userService.createUser(userRegistrationDTO);
        UserResponseDTO userResponseDTO = new UserResponseDTO(createdUser.getPlan().getName(), createdUser.getEmail());
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(createdUser.getId()).toUri();
        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @PostMapping("/complete-registration")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> completeRegistration(@RequestBody UserProfileCompletionDTO userProfileCompletionDTO, UriComponentsBuilder uriBuilder) {
        User user = userService.completeRegistration(userProfileCompletionDTO);
        UserDTO userDTO = new UserDTO(user.getPlan().getName(), user.getUsername());
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.ok().location(uri).body(userDTO);
    }

    @GetMapping("/")
    public ResponseEntity<Page<UserDTO>> newUser(Pageable pagination) {
        Page<UserDTO> UserDTOPage = userRepository.findByActiveTrue(pagination)
                .map(user -> new UserDTO(user.getPlan().getName(), user.getUsername()));
        return ResponseEntity.ok(UserDTOPage);
    }

    @PutMapping ("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> updateUser(@RequestBody UsernameChangeDTO usernameChangeDTO) {
        User user = userService.UpdateUsername(usernameChangeDTO);
        UserDTO userDTO = new UserDTO(user.getPlan().getName(), user.getUsername());
        return ResponseEntity.ok(userDTO);
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
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserInformationDTO> userInformation(@PathVariable Long id) {
        UserInformationDTO userInformationDTO = userService.userInformation(id);
        return ResponseEntity.ok(userInformationDTO);
    }
}
