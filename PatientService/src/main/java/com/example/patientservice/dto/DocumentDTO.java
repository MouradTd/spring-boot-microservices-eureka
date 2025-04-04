package com.example.patientservice.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private long id;
    private String title;
    private String attachement;
}
