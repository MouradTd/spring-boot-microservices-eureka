package com.example.ressourceservice.repository;

import com.example.ressourceservice.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalleRepository extends JpaRepository<Salle,Long> {
}