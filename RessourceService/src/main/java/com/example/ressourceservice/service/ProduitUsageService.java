package com.example.ressourceservice.service;

import com.example.ressourceservice.model.ProduitUsage;

import java.util.List;

public interface ProduitUsageService {
    void create(ProduitUsage produitUsage);
    ProduitUsage update(ProduitUsage produitUsage, long id);
    void delete(long id);
    List<ProduitUsage> findAll();
    ProduitUsage validateAndConsumeProduct(Long produitUsageId);
}
