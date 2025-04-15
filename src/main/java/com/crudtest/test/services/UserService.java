package com.crudtest.test.services;

import com.crudtest.test.dto.UserInformationDTO;
import com.crudtest.test.dto.UserProfileCompletionDTO;
import com.crudtest.test.dto.UserRegistrationDTO;
import com.crudtest.test.dto.UsernameChangeDTO;
import com.crudtest.test.model.Address;
import com.crudtest.test.model.Plan;
import com.crudtest.test.model.User;
import com.crudtest.test.repository.PlanRepository;
import com.crudtest.test.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    public UserService(UserRepository userRepository, PlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    private User mapToUser(UserRegistrationDTO userRegistrationDTO) {
        return User.builder()
                .email(userRegistrationDTO.email())
                .password(userRegistrationDTO.password())
                .createdAt(LocalDate.now())
                .build();
    }

    public User createUser(@Valid UserRegistrationDTO userRegistrationDTO) {
        long planDefaultId = 1L;
        Plan planDefault = planRepository.findById(planDefaultId).orElseThrow(() -> new RuntimeException("Plan not found"));
        User newUser = mapToUser(userRegistrationDTO);
        newUser.setPlan(planDefault);
        newUser.setActive(true);
        return userRepository.save(newUser);
    }

    public User completeRegistration(@Valid UserProfileCompletionDTO userProfileCompletionDTO) {
        User user = userRepository.findById(userProfileCompletionDTO.id()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userProfileCompletionDTO.firstName());
        user.setLastName(userProfileCompletionDTO.lastName());
        user.setUsername(userProfileCompletionDTO.username());
        user.setBirthDate(userProfileCompletionDTO.birthDate());
        user.setPhoneNumber(userProfileCompletionDTO.phoneNumber());
        user.setAddress(new Address(
                userProfileCompletionDTO.address().streetAddress(),
                userProfileCompletionDTO.address().city(),
                userProfileCompletionDTO.address().state(),
                userProfileCompletionDTO.address().zipCode(),
                userProfileCompletionDTO.address().country()
        ));
        return userRepository.save(user);
    }

    @Transactional
    public User UpdateUsername(@Valid UsernameChangeDTO usernameChangeDTO) {
        User user = userRepository.findById(usernameChangeDTO.id()).orElseThrow(() -> new RuntimeException("User not found"));
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
    public User deactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        return user;
    }


    public UserInformationDTO userInformation(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isActive()) {
            throw new IllegalArgumentException("User is not active");
        }
        return new UserInformationDTO(
                user.getPlan().getName(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getBirthDate(),
                user.getPhoneNumber()
        );
    }
}
