package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Machine;
import com.example.ressourceservice.repository.MachineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineServiceImp implements MachineService {
    private final MachineRepository machineRepository;

    public MachineServiceImp(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Override
    public void create(Machine machine) {
    }

    @Override
    public void update(Machine machine, long id) {
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public List<Machine> findAll() {
        return null;
    }
}
