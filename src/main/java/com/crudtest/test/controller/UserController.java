package com.crudtest.test.controller;

import com.crudtest.test.dto.NewUserDTO;
import com.crudtest.test.dto.UserProfileCompletionDTO;
import com.crudtest.test.dto.UserRegistrationDTO;
import com.crudtest.test.model.User;
import com.crudtest.test.repository.UserRepository;
import com.crudtest.test.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<User> createUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            User createdUser = userService.createUser(userRegistrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PostMapping("/{id}/complete-registration")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> completeRegistration(@PathVariable Long id, @RequestBody UserProfileCompletionDTO userProfileCompletionDTO) {
        try {
            User user = userService.completeRegistration(id, userProfileCompletionDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<NewUserDTO> newUSer() {;
        return userRepository.findAll().stream().map(user -> new NewUserDTO(user.getPlan().getName(), user.getUsername())).toList();
    }


}
