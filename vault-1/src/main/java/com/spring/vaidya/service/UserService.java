package com.spring.vaidya.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.vaidya.entity.User;
import com.spring.vaidya.exception.UserAlreadyExistsException;
import com.spring.vaidya.repo.UserRepository;

import java.util.Optional;

/**
 * Service class for managing User-related operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for UserService.
     *
     * @param userRepository The repository for user-related database operations.
     * @param passwordEncoder The password encoder for securely storing user passwords.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     * 
     * @param user The user object containing registration details.
     * @return The saved User object.
     * @throws UserAlreadyExistsException if a user with the given email already exists.
     */
    public User registerUser(User user) {
        // Check if email already exists
        if (userRepository.findByUserEmailIgnoreCase(user.getUserEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getUserEmail() + " already exists.");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to find.
     * @return An Optional containing the user if found, otherwise empty.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByUserEmailIgnoreCase(email);
    }
}
