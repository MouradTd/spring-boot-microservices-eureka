package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Produit;
import com.example.ressourceservice.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class ProduitServiceImp implements ProduitService {
    private final ProduitRepository produitRepository;

    public ProduitServiceImp(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public void create(Produit produit) {
        produitRepository.save(produit);
    }

    @Override
    public Produit update(Produit produit, long id) {
        Produit existingProduit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit not found with id: " + id));

        for (Field field : Produit.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("id")) { // Exclude the id field
                    Object newValue = field.get(produit);
                    if (newValue != null) {
                        field.set(existingProduit, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        return produitRepository.save(existingProduit);
    }

    @Override
    public void delete(long id) {
        produitRepository.deleteById(id);
    }

    @Override
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }
}
