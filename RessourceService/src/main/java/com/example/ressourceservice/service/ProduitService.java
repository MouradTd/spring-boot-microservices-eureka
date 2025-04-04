package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Produit;

import java.util.List;

public interface ProduitService {
    void create(Produit produit);
    Produit update(Produit produit, long id);
    void delete(long id);
    List<Produit> findAll();
    Produit findById(long id);
}
