package com.example.ressourceservice.controller;


import com.example.ressourceservice.model.Salle;
import com.example.ressourceservice.model.SalleAffectation;
import com.example.ressourceservice.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/ressource/salle")
public class SalleController {

    private final SalleService salleService;
    @Autowired
    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }


    @PostMapping("/")
    public  ResponseEntity<Salle> createSalle(@RequestBody Salle salle) {
        salleService.create(salle);
        return new ResponseEntity<>(salle, HttpStatus.CREATED);
    }


    @PostMapping("/affecter/{id}")
    public ResponseEntity<?> AffecterSalle(@RequestBody SalleAffectation salle, @PathVariable long id) {
        try {
            salleService.affecterSalle(salle, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to affecter salle: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to affecter salle: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
