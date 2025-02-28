package com.spring.vaidya.service;

import org.springframework.http.ResponseEntity;
import com.spring.vaidya.entity.User;

/**
 * Service interface for managing doctor-related operations.
 */
public interface DoctorService {

    /**
     * Saves a new doctor in the system.
     * 
     * @param doctor The doctor entity to be saved.
     * @return ResponseEntity with success message or error response.
     */
    ResponseEntity<?> saveDoctor(User doctor);

    /**
     * Confirms the doctor's email using the provided confirmation token.
     * 
     * @param confirmTokenDoctor The confirmation token sent via email.
     * @return ResponseEntity indicating success or failure of email confirmation.
     */
    ResponseEntity<?> confirmEmail(String confirmTokenDoctor);

    /**
     * Retrieves a doctor by their email address.
     * 
     * @param email The email of the doctor.
     * @return The User entity representing the doctor.
     */
    User getDoctorByEmail(String email);
}
