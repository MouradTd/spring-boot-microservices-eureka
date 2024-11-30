package com.example.factureservice.services;

import com.example.factureservice.dto.FactureDTO;

import java.util.List;

public interface FactureService {
    void create(FactureDTO facture);
    void update(FactureDTO facture, long id);
    void delete(long id);
    List<FactureDTO> findAll();
}
