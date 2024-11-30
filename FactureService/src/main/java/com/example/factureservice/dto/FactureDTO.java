package com.example.factureservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactureDTO {
//    private long id;
    private double amount;
    private String date;
    private String description;
    private long rdvid;
}
