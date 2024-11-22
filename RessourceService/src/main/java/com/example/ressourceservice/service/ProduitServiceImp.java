package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Produit;
import com.example.ressourceservice.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitServiceImp implements ProduitService {
    private final ProduitRepository produitRepository;

    public ProduitServiceImp(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public void create(Produit produit) {
    }

    @Override
    public void update(Produit produit, long id) {
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public List<Produit> findAll() {
        return null;
    }
}
