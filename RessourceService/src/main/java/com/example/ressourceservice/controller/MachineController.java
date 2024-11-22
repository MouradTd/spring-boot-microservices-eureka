package com.example.ressourceservice.controller;


import com.example.ressourceservice.service.MachineServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ressource/machine")
public class MachineController {

    private final MachineServiceImp machineService;
    @Autowired
    public MachineController(MachineServiceImp machineService) {
        this.machineService = machineService;
    }
}
