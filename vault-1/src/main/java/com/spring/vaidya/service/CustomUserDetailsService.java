package com.spring.vaidya.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.spring.vaidya.entity.User;
import com.spring.vaidya.repo.UserRepository;
import java.util.Optional;

/**
 * Custom implementation of UserDetailsService to load user details from the database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection for UserRepository.
     *
     * @param userRepository The repository used to fetch user details.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email (used as the username in authentication).
     *
     * @param username The email of the user.
     * @return A UserDetails object containing authentication details.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Fetch the User entity from the database
            Optional<User> userOptional = userRepository.findByUserEmailIgnoreCase(username);

            if (userOptional.isEmpty()) {
                System.err.println("User not found with email: " + username);
                throw new UsernameNotFoundException("User not found with email: " + username);
            }

            User user = userOptional.get();

            // Create and return a UserDetails object using Spring Security's User class
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserEmail()) // Set email as username
                    .password(user.getPassword())  // Store hashed password
                    .authorities("USER") // Set authorities; replace with actual roles if needed
                    .accountExpired(false) // Account is not expired
                    .accountLocked(false)  // Account is not locked
                    .credentialsExpired(false) // Credentials are valid
                    .disabled(!user.isEnabled()) // Check if user is enabled
                    .build();

        } catch (Exception e) {
            System.err.println("Error retrieving user details for: " + username + " - " + e.getMessage());
            throw new UsernameNotFoundException("Could not retrieve user details due to an internal error.");
        }
    }
}
