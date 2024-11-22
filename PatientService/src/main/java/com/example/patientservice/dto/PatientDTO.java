package com.example.patientservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
//    private long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
}
