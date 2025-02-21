package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Produit;
import com.example.ressourceservice.model.ProduitUsage;
import com.example.ressourceservice.repository.ProduitRepository;
import com.example.ressourceservice.repository.ProduitUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Service
public class ProduitUsageServiceImp implements ProduitUsageService {

    private final ProduitUsageRepository produitUsageRepository;
    private final ProduitRepository produitRepository;

    public ProduitUsageServiceImp(ProduitUsageRepository produitUsageRepository, ProduitRepository produitRepository) {
        this.produitUsageRepository = produitUsageRepository;
        this.produitRepository = produitRepository;
    }

    @Override
    public void create(ProduitUsage produitUsage) {
        produitUsageRepository.save(produitUsage);
    }

    @Override
    public ProduitUsage update(ProduitUsage produitUsage, long id) {
        ProduitUsage existingDemande = produitUsageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        for (Field field : ProduitUsage.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("id")) { // Exclude the id field
                    Object newValue = field.get(produitUsage);
                    if (newValue != null) {
                        field.set(existingDemande, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        return produitUsageRepository.save(existingDemande);
    }

    @Override
    public void delete(long id) {
        ProduitUsage usage = produitUsageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProduitUsage not found with id: " + id));

        if (!Objects.equals(usage.getStatus(), "pending")){
            throw new RuntimeException("Cannot delete a delivered product: " + usage);
        }
        produitUsageRepository.deleteById(id);
    }

    @Override
    public List<ProduitUsage> findAll() {
        return produitUsageRepository.findAll();
    }

    @Override
    public ProduitUsage validateAndConsumeProduct(Long produitUsageId) {
        ProduitUsage usage = produitUsageRepository.findById(produitUsageId)
                .orElseThrow(() -> new RuntimeException("ProduitUsage not found with id: " + produitUsageId));
        Produit produit = produitRepository.findById(usage.getProduit().getId())
                .orElseThrow(() -> new RuntimeException("Produit not found with id: " + usage.getProduit().getId()));

        if ((produit.getQuantity() - produit.getQte_used()) >= usage.getQuantity()) {
            produit.setQte_used(produit.getQte_used() + Long.valueOf(usage.getQuantity()));
            usage.setStatus("delivered");
            produitUsageRepository.save(usage);
            produitRepository.save(produit);
            return usage;
        } else {
            throw new RuntimeException("Not enough quantity for the product: " + produit.getName());
        }
    }

}
