package com.example.ressourceservice.repository;

import com.example.ressourceservice.model.ProduitUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitUsageRepository extends JpaRepository<ProduitUsage, Long> {

}
