package com.example.ressourceservice.repository;

import com.example.ressourceservice.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit,Long> {
}
