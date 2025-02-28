package com.spring.vaidya.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spring.vaidya.entity.User;
import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Extends JpaRepository to provide basic CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email, ignoring case sensitivity.
     *
     * @param email The email of the user.
     * @return An Optional containing the User entity if found, otherwise empty.
     */
    Optional<User> findByUserEmailIgnoreCase(String email);

    /**
     * Checks if a user with the given email exists.
     *
     * @param userEmail The email to check.
     * @return true if a user with the given email exists, otherwise false.
     */
    boolean existsByUserEmail(String userEmail);
}
