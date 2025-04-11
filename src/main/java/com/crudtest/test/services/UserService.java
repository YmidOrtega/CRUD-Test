package com.crudtest.test.services;

import com.crudtest.test.dto.UserProfileCompletionDTO;
import com.crudtest.test.dto.UserRegistrationDTO;
import com.crudtest.test.model.Address;
import com.crudtest.test.model.Plan;
import com.crudtest.test.model.User;
import com.crudtest.test.repository.PlanRepository;
import com.crudtest.test.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

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

    public User createUser(UserRegistrationDTO userRegistrationDTO) {
        long planDefaultId = 1L;
        Plan planDefault = planRepository.findById(planDefaultId).orElseThrow(() -> new RuntimeException("Plan not found"));
        User newUser = mapToUser(userRegistrationDTO);
        newUser.setPlan(planDefault);
        return userRepository.save(newUser);
    }

    public User completeRegistration(Long id, UserProfileCompletionDTO userProfileCompletionDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
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
}
