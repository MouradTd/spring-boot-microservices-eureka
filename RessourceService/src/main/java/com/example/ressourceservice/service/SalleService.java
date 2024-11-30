package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Salle;
import com.example.ressourceservice.model.SalleAffectation;

import java.util.List;

public interface SalleService {
    void create(Salle salle);
    void update(Salle salle, long id);
    void delete(long id);
    List<Salle> findAll();
    void affecterSalle(SalleAffectation salle, long id);
}
