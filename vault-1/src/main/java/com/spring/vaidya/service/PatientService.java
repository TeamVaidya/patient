package com.spring.vaidya.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.spring.vaidya.entity.Patient;

/**
 * Service interface for managing Patient-related operations.
 */
public interface PatientService {

    /**
     * Saves a new patient.
     * 
     * @param patient The patient entity to be saved.
     * @return The saved Patient object.
     */
    Patient savePatient(Patient patient);

    /**
     * Retrieves all patients from the system.
     * 
     * @return A list of all patients.
     */
    List<Patient> getAllPatients();

    /**
     * Retrieves a patient by their unique ID.
     * 
     * @param id The ID of the patient.
     * @return The Patient object if found.
     */
    Patient getPatientById(Long id);

    /**
     * Updates an existing patient record.
     * 
     * @param id The ID of the patient to be updated.
     * @param patient The updated patient information.
     * @return The updated Patient object.
     */
    Patient updatePatient(Long id, Patient patient);

    /**
     * Deletes a patient by their unique ID.
     * 
     * @param id The ID of the patient to be deleted.
     */
    void deletePatient(Long id);

    /**
     * Retrieves patients based on their mobile number.
     * 
     * @param mobileNo The mobile number to search for.
     * @return A list of patients with the given mobile number.
     */
    List<Patient> getPatientsByphoneNumber(String phoneNumber);

    /**
     * Retrieves a patient based on their slot ID.
     * 
     * @param slotId The slot ID to search for.
     * @return An optional containing the Patient object if found.
     */
    Optional<Patient> getPatientBySlotId(Long slotId);

    /**
     * Retrieves all patients assigned to a specific doctor.
     * 
     * @param userId The ID of the doctor.
     * @return A list of patients associated with the given doctor.
     */
    List<Patient> getPatientsByDoctorUserId(Long userId);

    /**
     * Retrieves all patients assigned to a specific doctor on a given date.
     * 
     * @param userId The ID of the doctor.
     * @param date The date for which to retrieve patients.
     * @return A list of patients for the specified doctor on the given date.
     */
    List<Patient> getPatientsByDoctorUserIdAndDate(Long userId, LocalDate date);


}
