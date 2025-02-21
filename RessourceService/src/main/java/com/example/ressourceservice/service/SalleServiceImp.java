package com.example.ressourceservice.service;

import com.example.ressourceservice.model.Salle;
import com.example.ressourceservice.model.SalleAffectation;
import com.example.ressourceservice.repository.SalleAffectationRepository;
import com.example.ressourceservice.repository.SalleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class SalleServiceImp implements SalleService {
    private final SalleRepository salleRepository;
    private final SalleAffectationRepository salleAffectationRepository;
    private static final Logger logger = LoggerFactory.getLogger(SalleServiceImp.class);
    @PersistenceContext
    private EntityManager entityManager;
    public SalleServiceImp(SalleRepository salleRepository, SalleAffectationRepository salleAffectationRepository) {
        this.salleRepository = salleRepository;
        this.salleAffectationRepository = salleAffectationRepository;
    }

    @Override
    public void create(Salle salle) {
        salleRepository.save(salle);
    }

    @Override
    public Salle update(Salle salle, long id) {
        Salle existingSalle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found with id: " + id));

        for (Field field : Salle.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("id")) { // Exclude the id field
                    Object newValue = field.get(salle);
                    if (newValue != null) {
                        field.set(existingSalle, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        return salleRepository.save(existingSalle);
    }

    @Override
    public void delete(long id) {
        Salle existingSalle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found with id: " + id));

        List<SalleAffectation> pendingAffectations = salleAffectationRepository.findBySalleIdAndStatus(id, "pending");
        if (!pendingAffectations.isEmpty()) {
            throw new RuntimeException("Cannot delete salle with id: " + id + " as it has pending Appointements.");
        }

        salleRepository.delete(existingSalle);
    }

    @Override
    public List<Salle> findAll() {
        return salleRepository.findAll();
    }

    @Override
    @Transactional
    public void affecterSalle(SalleAffectation salleAffectation, long id) {
        Salle existingSalle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found with id: " + id));
        salleAffectation.setSalle(existingSalle);
        entityManager.persist(salleAffectation);
    }

    @Transactional
    public void updateSalleAffectationStatus(Long appointmentId, Long salleId, String status) {
        logger.info("Updating salle affectation status. AffectationId: {}, SalleId: {}, Status: {}", appointmentId, salleId, status);
        try {
            TypedQuery<SalleAffectation> query = entityManager.createQuery(
                    "SELECT sa FROM SalleAffectation sa WHERE sa.appointmentId = :appointmentId AND sa.salle.id = :salleId",
                    SalleAffectation.class);
            query.setParameter("appointmentId", appointmentId);
            query.setParameter("salleId", salleId);
            SalleAffectation salleAffectation = query.getSingleResult();
            logger.info("Found SalleAffectation: {}", salleAffectation);
            salleAffectation.setStatus(status);
            entityManager.merge(salleAffectation);
            logger.info("Updated SalleAffectation status to: {}", status);
        } catch (NoResultException e) {
            logger.error("No SalleAffectation found with affectationId: {} and salleId: {}", appointmentId, salleId, e);
            throw new RuntimeException("No SalleAffectation found with affectationId: " + appointmentId + " and salleId: " + salleId, e);
        }
    }
}
