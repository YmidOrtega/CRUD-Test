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
        try {
            User createdUser = userService.createUser(userRegistrationDTO);
            UserResponseDTO userResponseDTO = new UserResponseDTO(createdUser.getPlan().getName(), createdUser.getEmail());
            URI uri = UriComponentsBuilder.fromPath("/user/{id}").buildAndExpand(createdUser.getId()).toUri();
            return ResponseEntity.created(uri).body(userResponseDTO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping("/complete-registration")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> completeRegistration(@RequestBody UserProfileCompletionDTO userProfileCompletionDTO, UriComponentsBuilder uriBuilder) {
        try {
            User user = userService.completeRegistration(userProfileCompletionDTO);
            UserDTO userDTO = new UserDTO(user.getPlan().getName(), user.getUsername());
            URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
            return ResponseEntity.ok(userDTO).(uri).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        try {
            User user = userService.UpdateUsername(usernameChangeDTO);
            UserDTO userDTO = new UserDTO(user.getPlan().getName(), user.getUsername());
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

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
        try {
            userService.deactivateUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
