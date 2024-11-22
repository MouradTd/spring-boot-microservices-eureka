package com.example.ressourceservice.repository;

import com.example.ressourceservice.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine,Long> {
}
