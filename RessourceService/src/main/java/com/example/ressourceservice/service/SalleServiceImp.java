package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Salle;
import com.example.ressourceservice.repository.SalleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalleServiceImp implements SalleService {
    private final SalleRepository salleRepository;

    public SalleServiceImp(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    @Override
    public void create(Salle salle) {
    }

    @Override
    public void update(Salle salle, long id) {
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public List<Salle> findAll() {
        return null;
    }

}
