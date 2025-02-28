package com.spring.vaidya.entity;


import java.time.LocalDateTime;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Schema(description = "Represents a patient entity with details such as name, age, contact information, and associated doctor and slot.")
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the patient", example = "1")
    private Long patientId;
    
    @Schema(description = "Full name of the patient", example = "John Doe")
    private String patientName;
    
    @Schema(description = "Mobile number of the patient", example = "9876543210")
    private String phoneNumber;
    
    @Schema(description = "Email address of the patient", example = "johndoe@example.com")
    private String email;
    
    @Schema(description = "Aadhar number of the patient", example = "123456789012")
    private Long aadharNo;
    
    @Schema(description = "Age of the patient", example = "30")
    private Integer age;
    
    @Schema(description = "Date and time of patient registration", example = "2024-02-24T12:30:00")
    private LocalDateTime dateTime;
    
    @Schema(description = "Residential address of the patient", example = "123, Green Street, NY")
    private String address;
    
    @Schema(description = "Role ID associated with the patient", example = "2")
    private Integer roleId;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    @Schema(description = "Doctor associated with the patient")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "slotId")
    @Schema(description = "Slot booked by the patient")
    private Slot slot;

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getphoneNumber() {
		return phoneNumber;
	}

	public void setphoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(Long aadharNo) {
		this.aadharNo = aadharNo;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public User getDoctor() {
		return doctor;
	}

	public void setDoctor(User doctor) {
		this.doctor = doctor;
	}

	public Slot getSlot() {
		return slot;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aadharNo, address, age, dateTime, doctor, email, phoneNumber, patientId, patientName, roleId,
				slot);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		return Objects.equals(aadharNo, other.aadharNo) && Objects.equals(address, other.address)
				&& Objects.equals(age, other.age) && Objects.equals(dateTime, other.dateTime)
				&& Objects.equals(doctor, other.doctor) && Objects.equals(email, other.email)
				&& Objects.equals(phoneNumber, other.phoneNumber) && Objects.equals(patientId, other.patientId)
				&& Objects.equals(patientName, other.patientName) && Objects.equals(roleId, other.roleId)
				&& Objects.equals(slot, other.slot);
	}

	@Override
	public String toString() {
		return "Patient [patientId=" + patientId + ", patientName=" + patientName + ", phoneNumber=" + phoneNumber
				+ ", email=" + email + ", aadharNo=" + aadharNo + ", age=" + age + ", dateTime=" + dateTime
				+ ", address=" + address + ", roleId=" + roleId + ", doctor=" + doctor + ", slot=" + slot + "]";
	}

	public Patient(Long patientId, String patientName, String phoneNumber, String email, Long aadharNo, Integer age,
			LocalDateTime dateTime, String address, Integer roleId, User doctor, Slot slot) {
		super();
		this.patientId = patientId;
		this.patientName = patientName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.aadharNo = aadharNo;
		this.age = age;
		this.dateTime = dateTime;
		this.address = address;
		this.roleId = roleId;
		this.doctor = doctor;
		this.slot = slot;
	}

	public Patient() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
