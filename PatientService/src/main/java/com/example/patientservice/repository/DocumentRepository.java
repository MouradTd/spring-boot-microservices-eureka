package com.example.patientservice.repository;

import com.example.patientservice.model.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Documents, Long> {
    List<Documents> findByPatientId(long patientId);
}
