package com.example.patientservice.model;

import com.example.patientservice.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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




}