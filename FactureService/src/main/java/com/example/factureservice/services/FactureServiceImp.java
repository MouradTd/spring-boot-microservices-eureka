package com.example.factureservice.services;

import com.example.factureservice.dto.FactureDTO;
import com.example.factureservice.model.Facture;
import com.example.factureservice.repositories.FactureRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactureServiceImp implements FactureService {

    private final FactureRepository factureRepository;

    public FactureServiceImp(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    @Override
    public void create(FactureDTO facture) {
    }

    @Override
    public void update(FactureDTO facture, long id) {
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public List<FactureDTO> findAll() {
        return null;
    }

    private FactureDTO convertToDTO(Facture facture) {
        FactureDTO factureDTO = new FactureDTO();
        BeanUtils.copyProperties(facture, factureDTO);
        return factureDTO;
    }
}
