package com.example.ressourceservice.controller;


import com.example.ressourceservice.service.MachineServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ressource/produit")
public class ProduitController {

    private final MachineServiceImp machineService;
    @Autowired
    public ProduitController(MachineServiceImp machineService) {
        this.machineService = machineService;
    }

}
