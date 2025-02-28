package com.spring.vaidya.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.vaidya.entity.User;

/**
 * Repository interface for handling database operations related to the Doctor entity.
 * Extends JpaRepository to provide basic CRUD operations.
 */
public interface DoctorRepository extends JpaRepository<User, Long> {

    /**
     * Finds a doctor by email (case-insensitive).
     *
     * @param emailId The email of the doctor.
     * @return The User entity representing the doctor, if found.
     */
    User findByUserEmailIgnoreCase(String emailId);

    /**
     * Checks if a doctor exists with the given email.
     *
     * @param email The email to check.
     * @return True if a doctor exists with the given email, false otherwise.
     */
    Boolean existsByUserEmail(String email);

    /**
     * Finds a doctor by email.
     *
     * @param email The email of the doctor.
     * @return The User entity representing the doctor, if found.
     */
    User findByUserEmail(String email);

    /**
     * Finds a doctor by full name.
     *
     * @param fullName The full name of the doctor.
     * @return An Optional containing the User entity representing the doctor, if found.
     */
    Optional<User> findByFullName(String fullName);
}
