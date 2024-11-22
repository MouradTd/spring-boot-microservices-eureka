package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Machine;

import java.util.List;

public interface MachineService {
    void create(Machine machine);
    void update(Machine machine, long id);
    void delete(long id);
    List<Machine> findAll();
}
