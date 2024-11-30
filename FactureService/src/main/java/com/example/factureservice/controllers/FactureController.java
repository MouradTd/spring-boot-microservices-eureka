package com.example.factureservice.controllers;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.services.FactureServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Facture", description = "the Facture Api")
@RestController
@RequestMapping("/api/facture")
public class FactureController {
    private final FactureServiceImp factureService;

    public FactureController(FactureServiceImp factureService) {
        this.factureService = factureService;
    }

    @Operation(
            summary = "Fetch all factures",
            description = "fetches all factures from the database")
    @GetMapping("/")
    public List<FactureDTO> getAllFactures() {
        return factureService.findAll();
    }

    @Operation(
            summary = "Create a facture",
            description = "Create a facture in the database")
    @PostMapping("/")
    public ResponseEntity<FactureDTO> createFacture(FactureDTO facture) {
        factureService.create(facture);
        return new ResponseEntity<>(facture, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update a facture",
            description = "Update a facture in the database")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFacture(@RequestBody FactureDTO patient, @PathVariable long id) {
        try {
            factureService.update(patient, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update facture: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Delete a facture",
            description = "Delete a facture in the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacture(@PathVariable long id) {
        try {
            factureService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete facture: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
