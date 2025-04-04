package com.example.ressourceservice.controller;


import com.example.ressourceservice.model.Produit;
import com.example.ressourceservice.model.ProduitUsage;
import com.example.ressourceservice.service.MachineServiceImp;
import com.example.ressourceservice.service.ProduitServiceImp;
import com.example.ressourceservice.service.ProduitUsageServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ressource/product")
public class ProduitController {

    private final ProduitServiceImp produitService;
    private final ProduitUsageServiceImp produitUsageService;
    @Autowired
    public ProduitController(ProduitServiceImp produitService, ProduitUsageServiceImp produitUsageService) {
        this.produitService = produitService;
        this.produitUsageService = produitUsageService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Produit>> getAllProducts() {
        List<Produit> patients = produitService.findAll();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Produit> createProduct(@RequestBody Produit produit) {
        produitService.create(produit);
        return new ResponseEntity<>(produit, HttpStatus.CREATED);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePatient(@RequestBody Produit patient, @PathVariable long id) {
        try {
            Produit updatedPatient = produitService.update(patient, id);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update produit: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable long id) {
        try {
            produitService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete produit: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Usage consumption Logic

    @GetMapping("/usage/get")
    public ResponseEntity<List<ProduitUsage>> getAllProduitUsages() {
        List<ProduitUsage> produitUsages = produitUsageService.findAll();
        return new ResponseEntity<>(produitUsages, HttpStatus.OK);
    }

    @PostMapping("/usage/create")
    public ResponseEntity<ProduitUsage> createProduitUsage(@RequestBody Map<String, Object> payload) {
        Long produitId = Long.valueOf(payload.get("produit_id").toString());
        Produit produit = produitService.findById(produitId);

        ProduitUsage produitUsage = new ProduitUsage();
        produitUsage.setProduit(produit);
        produitUsage.setQuantity(Integer.valueOf(payload.get("quantity").toString()));
        produitUsage.setStatus(payload.get("status").toString());
        produitUsage.setUsageDescription(payload.get("usageDescription").toString());

        produitUsageService.create(produitUsage);
        return new ResponseEntity<>(produitUsage, HttpStatus.CREATED);
    }

    @PutMapping("/usage/update/{id}")
    public ResponseEntity<?> updateProduitUsage(@RequestBody ProduitUsage produitUsage, @PathVariable long id) {
        try {
            ProduitUsage updatedProduitUsage = produitUsageService.update(produitUsage, id);
            return new ResponseEntity<>(updatedProduitUsage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update produit usage: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/usage/delete/{id}")
    public ResponseEntity<?> deleteProduitUsage(@PathVariable long id) {
        try {
            produitUsageService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete produit usage: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/usage/validate/{id}")
    public ResponseEntity<?> validateAndConsumeProduct(@PathVariable long id) {
        try {
            ProduitUsage usage = produitUsageService.validateAndConsumeProduct(id);
            return new ResponseEntity<>(usage, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("Failed to update produit usage: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
