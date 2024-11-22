package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String email;
    private String password;
    @NonNull
    private String poste;

    private String phoneNumber;
    private String address;

    // Additional fields specific to dentists
    private String specialization;
    private String licenseNumber;

    // Additional fields specific to back office desk employees
    private String department;

    public void setRoles(Set<Role> adminRoles) {
    }
}
