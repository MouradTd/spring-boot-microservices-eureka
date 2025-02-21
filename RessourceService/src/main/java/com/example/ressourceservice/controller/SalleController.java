package com.example.ressourceservice.controller;

    import com.example.ressourceservice.dto.SalleAffectationRequest;
    import com.example.ressourceservice.model.Salle;
    import com.example.ressourceservice.model.SalleAffectation;
    import com.example.ressourceservice.service.SalleServiceImp;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/ressource/salle")
    public class SalleController {

        private final SalleServiceImp salleService;
        private final SalleServiceImp salleServiceImp;

        @Autowired
        public SalleController(SalleServiceImp salleService, SalleServiceImp salleServiceImp) {
            this.salleService = salleService;
            this.salleServiceImp = salleServiceImp;
        }

        @PostMapping("/insert")
        public ResponseEntity<Salle> createSalle(@RequestBody Salle salle) {
            salleService.create(salle);
            return new ResponseEntity<>(salle, HttpStatus.CREATED);
        }

        @GetMapping("/get")
        public ResponseEntity<List<Salle>> findAll() {
            List<Salle> salles = salleService.findAll();
            return new ResponseEntity<>(salles, HttpStatus.OK);
        }

        @PutMapping("/update/{id}")
        public ResponseEntity<?> update(@RequestBody Salle salle,@PathVariable int id) {
            try {
                Salle updatedPatient = salleServiceImp.update(salle, id);
                return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to update salle: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<?> deleteSalle(@PathVariable long id) {
            try {
                salleServiceImp.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to delete patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @PostMapping("/affecter/{id}")
        public ResponseEntity<?> affecterSalle(@RequestBody SalleAffectation salle, @PathVariable long id) {
            try {
                salleService.affecterSalle(salle, id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>("Failed to affecter salle: " + e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to affecter salle: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @PostMapping("/validate")
        public ResponseEntity<?> updateSalleAffectationStatus(@RequestBody SalleAffectationRequest request) {
            try {
                salleService.updateSalleAffectationStatus(request.getAppointementId(), request.getSalleId(), request.getStatus());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>("Failed to update salle appointement status: " + e.getMessage(), HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to update salle affectation status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }