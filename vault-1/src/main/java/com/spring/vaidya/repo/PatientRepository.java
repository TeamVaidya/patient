package com.spring.vaidya.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.vaidya.entity.Patient;

/**
 * Repository interface for managing Patient entities.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Finds a list of patients by their mobile number.
     *
     * @param mobileNo The mobile number of the patient.
     * @return List of patients associated with the given mobile number.
     */
	List<Patient> findByphoneNumber(String phoneNumber);

    /**
     * Finds a patient by the associated slot ID.
     *
     * @param slotId The ID of the slot.
     * @return Optional containing the patient if found, otherwise empty.
     */
    Optional<Patient> findBySlot_SlotId(Long slotId);

    /**
     * Finds all patients assigned to a specific doctor by the doctor's user ID.
     *
     * @param userId The user ID of the doctor.
     * @return List of patients associated with the given doctor.
     */
    List<Patient> findByDoctorUserId(Long userId);

    /**
     * Finds patients assigned to a doctor on a specific date by filtering appointments within the day's range.
     *
     * @param userId     The user ID of the doctor.
     * @param startOfDay The start of the day (00:00 AM).
     * @param endOfDay   The end of the day (11:59 PM).
     * @return List of patients scheduled within the specified date range.
     */
    List<Patient> findByDoctorUserIdAndDateTimeBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    /**
     * Finds the first patient by the associated slot ID.
     * 
     * @param slotId The ID of the slot.
     * @return Optional containing the first matching patient, if found.
     */
    Optional<Patient> findFirstBySlot_SlotId(Long slotId);

	
}
