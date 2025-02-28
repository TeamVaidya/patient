package com.spring.vaidya.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.vaidya.entity.ErrorResponse;
import com.spring.vaidya.entity.Patient;
import com.spring.vaidya.service.PatientService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for managing patient-related operations.
 */
@Tag(name = "Patient Management", description = "APIs for managing patients")
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:5173")
@OpenAPIDefinition (info=@io.swagger.v3.oas.annotations.info.Info(title="Patient registeration and management API", description="APIs for registering and managing patients"))
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
    private final PatientService patientService;

    /**
     * Constructor for injecting dependencies.
     * 
     * @param patientService Service layer for patient operations.
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Creates a new patient.
     * 
     * @param patient Patient object to be created.
     * @return ResponseEntity containing the created patient or error response.
     */
    @Operation(summary = "Create a new patient", description = "Creates and saves a new patient record.")
    @ApiResponse(responseCode = "201", description = "Patient created successfully", content = @Content(schema = @Schema(implementation = Patient.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping("/post")
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Patient savedPatient = patientService.savePatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            logger.error("Error creating patient: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                            "Failed to create patient", e.getMessage()));
        }
    }

    /**
     * Retrieves all patients.
     * 
     * @return List of all patients.
     */
    @Operation(summary = "Get all patients", description = "Retrieves a list of all patients.")
    @ApiResponse(responseCode = "200", description = "List of patients", content = @Content(schema = @Schema(implementation = Patient.class)))
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves a patient by their ID.
     * 
     * @param id Patient ID.
     * @return ResponseEntity containing the patient or error response.
     */
    @Operation(summary = "Get a patient by ID", description = "Retrieves a patient using their ID.")
    @ApiResponse(responseCode = "200", description = "Patient found", content = @Content(schema = @Schema(implementation = Patient.class)))
    @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            logger.error("Patient with ID {} not found: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                            "Patient not found", e.getMessage()));
        }
    }

    /**
     * Updates an existing patient.
     * 
     * @param id Patient ID.
     * @param patient Updated patient details.
     * @return ResponseEntity containing updated patient or error response.
     */
    @Operation(summary = "Update a patient", description = "Updates an existing patient record.")
    @ApiResponse(responseCode = "200", description = "Patient updated successfully", content = @Content(schema = @Schema(implementation = Patient.class)))
    @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            logger.error("Error updating patient with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                            "Failed to update patient", e.getMessage()));
        }
    }

    /**
     * Deletes a patient by ID.
     * 
     * @param id Patient ID.
     * @return ResponseEntity indicating success or failure.
     */
    @Operation(summary = "Delete a patient", description = "Deletes a patient using their ID.")
    @ApiResponse(responseCode = "204", description = "Patient deleted successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting patient with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                                            "Failed to delete patient", e.getMessage()));
        }
    }

    /**
     * Retrieves patients by their mobile number.
     * 
     * @param phoneNumber Mobile number of the patient(s).
     * @return ResponseEntity containing a list of patients or error response.
     */
    @Operation(summary = "Get patients by phone number", description = "Retrieves patients using their phone number.")
    @ApiResponse(responseCode = "200", description = "Patients found", content = @Content(schema = @Schema(implementation = Patient.class)))
    @ApiResponse(responseCode = "404", description = "No patients found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/search1")
    public ResponseEntity<?> getPatientsByphoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        List<Patient> patients = patientService.getPatientsByphoneNumber(phoneNumber);
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                            "No patients found with this mobile number", ""));
        }
        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves a patient by their slot ID.
     * 
     * @param slotId Slot ID associated with the patient.
     * @return ResponseEntity containing the patient or not found status.
     */
    @Operation(summary = "Get patient by slot ID", description = "Retrieves a patient using their slot ID.")
    @ApiResponse(responseCode = "200", description = "Patient found", content = @Content(schema = @Schema(implementation = Patient.class)))
    @ApiResponse(responseCode = "404", description = "Patient not found")
    @GetMapping("/slot/{slotId}")
    public ResponseEntity<Patient> getPatientBySlotId(@PathVariable Long slotId) {
        Optional<Patient> patient = patientService.getPatientBySlotId(slotId);
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves patients by the doctor’s user ID.
     * 
     * @param userId Doctor's user ID.
     * @return ResponseEntity containing a list of patients or error response.
     */
    @Operation(summary = "Get patients by doctor ID", description = "Retrieves patients associated with a doctor by their user ID.")
    @ApiResponse(responseCode = "200", description = "Patients found", content = @Content(schema = @Schema(implementation = Patient.class)))
    @ApiResponse(responseCode = "404", description = "No patients found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/doctor/{userId}")
    public ResponseEntity<?> getPatientsByDoctorUserId(@PathVariable Long userId) {
        List<Patient> patients = patientService.getPatientsByDoctorUserId(userId);
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                            "No patients found for this doctor", ""));
        }
        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves patients by the doctor’s user ID and appointment date.
     * 
     * @param userId Doctor's user ID.
     * @param date Appointment date in ISO format (YYYY-MM-DD).
     * @return ResponseEntity containing a list of patients or error response.
     */
    @Operation(summary = "Get patients by doctor ID, user ID and date", description = "Retrieves patients associated with a doctor by their user ID and date.")
    @ApiResponse(responseCode = "200", description = "Patients found", content = @Content(schema = @Schema(implementation = Patient.class)))
    @ApiResponse(responseCode = "404", description = "No patients found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/doctor/{userId}/date/{date}")
    public ResponseEntity<?> getPatientsByDoctorUserIdAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date.trim());
            List<Patient> patients = patientService.getPatientsByDoctorUserIdAndDate(userId, parsedDate);

            if (patients.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), 
                                                "No patients found for this doctor on this date", ""));
            }
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            logger.error("Error parsing date {}: {}", date, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), 
                                            "Invalid date format", e.getMessage()));
        }
    }
}
