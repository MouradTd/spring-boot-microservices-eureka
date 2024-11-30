package com.example.patientservice.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

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
    private List<Map<String, Object>> appointments;
}
