package com.example.patientservice.model;

import com.example.patientservice.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String dateOfBirth;
    private String medicalHistory;
    private String allergies;
    private String emergencyContact;
    private String insuranceInformation;
    private String primaryCarePhysician;

    public Patient() {
    }

    public Patient(long id, String name, String address, String email, String phone, int age, Gender gender, String dateOfBirth, String medicalHistory, String allergies, String emergencyContact, String insuranceInformation, String primaryCarePhysician) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;
        this.emergencyContact = emergencyContact;
        this.insuranceInformation = insuranceInformation;
        this.primaryCarePhysician = primaryCarePhysician;
    }
}