package com.example.patientservice.dto;

import com.example.patientservice.enums.Gender;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private int age;
    private String address;
    private Gender gender;
    private String dateOfBirth;
    private String emergencyContact;
    private String insuranceInformation;
    private String primaryCarePhysician;
    private String medicalHistory;
    private String allergies;
    private List<Map<String, Object>> appointments;
    private List<DocumentDTO> documents;
}
