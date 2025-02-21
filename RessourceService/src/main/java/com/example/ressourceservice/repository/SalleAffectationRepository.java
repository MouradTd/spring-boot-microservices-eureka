package com.example.ressourceservice.repository;

import com.example.ressourceservice.model.SalleAffectation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalleAffectationRepository extends JpaRepository<SalleAffectation, Long> {
    List<SalleAffectation> findBySalleIdAndStatus(Long salleId, String status);
}
