package com.spring.vaidya.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.spring.vaidya.entity.Patient;
import com.spring.vaidya.entity.Slot;
import com.spring.vaidya.entity.User;
import com.spring.vaidya.exception.SlotAlreadyBookedException;
import com.spring.vaidya.repo.DoctorRepository;
import com.spring.vaidya.repo.PatientRepository;
import com.spring.vaidya.repo.SlotRepository;
import com.spring.vaidya.service.PatientService;

/**
 * Implementation of PatientService to handle patient-related operations.
 */
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final SlotRepository slotRepository;

    /**
     * Constructor to initialize repositories.
     *
     * @param patientRepository Repository for patient-related database operations.
     * @param doctorRepository Repository for doctor-related database operations.
     * @param slotRepository Repository for slot-related database operations.
     */
    public PatientServiceImpl(PatientRepository patientRepository, DoctorRepository doctorRepository, SlotRepository slotRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.slotRepository = slotRepository;
    }

    /**
     * Saves a new patient after validating the doctor and slot availability.
     *
     * @param patient The patient entity to be saved.
     * @return The saved patient entity.
     * @throws RuntimeException If the doctor or slot is not found, or the slot is already booked.
     */
    @Override
    public Patient savePatient(Patient patient) {
        // ✅ Validate input patient data
        if (patient == null || patient.getDoctor() == null || patient.getSlot() == null) {
            throw new IllegalArgumentException("Invalid patient details. Doctor and Slot information are required.");
        }

        // ✅ Validate Email, Phone Number, and Aadhar
        validatePatientDetails(patient);

        // ✅ Fetch the Doctor entity
        User doctor = doctorRepository.findById(patient.getDoctor().getUserId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + patient.getDoctor().getUserId()));

        // ✅ Fetch the Slot entity
        Slot slot = slotRepository.findById(patient.getSlot().getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found with ID: " + patient.getSlot().getSlotId()));

        // ✅ Check if the slot is already booked
        if (patientRepository.findFirstBySlot_SlotId(slot.getSlotId()).isPresent()) {
            throw new SlotAlreadyBookedException("Slot ID: " + slot.getSlotId() + " is already booked. Please choose another slot.");
        }

        // ✅ Check if the slot is available
        if (!"yes".equalsIgnoreCase(slot.getStatus())) {
            throw new RuntimeException("Slot ID: " + slot.getSlotId() + " is not available for booking.");
        }

        // ✅ Assign the doctor and slot to the patient
        patient.setDoctor(doctor);
        patient.setSlot(slot);

        // ✅ Mark the slot as booked and save it
        slot.setStatus("no");
        slotRepository.save(slot);

        // ✅ Save and return the patient entity
        return patientRepository.save(patient);
    }

    /**
     * ✅ Validates patient's email, phone number, and Aadhar number
     */
    private void validatePatientDetails(Patient patient) {
        if (!isValidEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Invalid email format: " + patient.getEmail());
        }

        if (!isValidPhoneNumber(patient.getphoneNumber())) { // Fixed method name
            throw new IllegalArgumentException("Invalid phone number format: " + patient.getphoneNumber());
        }

        if (!isValidAadhar(String.valueOf(patient.getAadharNo()))) { // Convert Long to String
            throw new IllegalArgumentException("Invalid Aadhar number format: " + patient.getAadharNo());
        }
    }


    /**
     * ✅ Validates email format
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * ✅ Validates phone number format (10 digits)
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[6-9]\\d{9}$"; // Ensures it starts with 6-9 and has 10 digits
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }

    /**
     * ✅ Validates Aadhar number format (12 digits)
     */
    private boolean isValidAadhar(String aadharNumber) {
        String aadharRegex = "^[2-9]\\d{11}$"; // Ensures it has 12 digits and doesn't start with 0 or 1
        return aadharNumber != null && aadharNumber.matches(aadharRegex);
    }


    /**
     * Retrieves all patients.
     *
     * @return List of all patients.
     */
    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Retrieves a patient by ID.
     *
     * @param id The patient ID.
     * @return The patient entity.
     * @throws RuntimeException If no patient is found with the given ID.
     */
    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id " + id));
    }

    /**
     * Updates an existing patient's details.
     *
     * @param id The patient ID.
     * @param patientDetails Updated patient details.
     * @return The updated patient entity.
     */
    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = getPatientById(id);
        patient.setPatientName(patientDetails.getPatientName());
        patient.setphoneNumber(patientDetails.getphoneNumber());
        patient.setEmail(patientDetails.getEmail());
        patient.setAadharNo(patientDetails.getAadharNo());
        patient.setAge(patientDetails.getAge());
        patient.setDateTime(patientDetails.getDateTime());
        patient.setAddress(patientDetails.getAddress());
        patient.setRoleId(patientDetails.getRoleId());
        return patientRepository.save(patient);
    }

    /**
     * Deletes a patient by ID.
     *
     * @param id The patient ID.
     */
    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    /**
     * Retrieves patients by mobile number.
     *
     * @param phoneNumber The mobile number of the patient.
     * @return List of patients with the given mobile number.
     */
    @Override
    public List<Patient> getPatientsByphoneNumber(String phoneNumber) {
        return patientRepository.findByphoneNumber(phoneNumber);
    }

    /**
     * Retrieves a patient by slot ID.
     *
     * @param slotId The slot ID.
     * @return An Optional containing the patient if found.
     */
    public Optional<Patient> getPatientBySlotId(Long slotId) {
        return patientRepository.findBySlot_SlotId(slotId);
    }

    /**
     * Retrieves patients by doctor's user ID.
     *
     * @param userId The doctor’s user ID.
     * @return List of patients associated with the given doctor.
     */
    @Override
    public List<Patient> getPatientsByDoctorUserId(Long userId) {
        return patientRepository.findByDoctorUserId(userId);
    }

    /**
     * Retrieves patients by doctor ID and appointment date.
     *
     * @param userId The doctor’s user ID.
     * @param date The appointment date.
     * @return List of patients for the given doctor on the specified date.
     */
    @Override
    public List<Patient> getPatientsByDoctorUserIdAndDate(Long userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return patientRepository.findByDoctorUserIdAndDateTimeBetween(userId, startOfDay, endOfDay);
    }
}
