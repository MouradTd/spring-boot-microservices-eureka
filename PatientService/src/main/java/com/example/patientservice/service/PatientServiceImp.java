package com.example.patientservice.service;

import com.example.patientservice.dto.DocumentDTO;
import com.example.patientservice.dto.PatientDTO;
import com.example.patientservice.ecxeption.BadRequestException;
import com.example.patientservice.model.Documents;
import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.DocumentRepository;
import com.example.patientservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
//@AllArgsConstructor
public class PatientServiceImp implements PatientService {
    private final PatientRepository patientRepository;
    private final DocumentRepository documentsRepository;
    // to make calls between services in http requests
//    @Autowired
    private final WebClient.Builder webClient;
    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImp.class);

//    @Autowired
    public PatientServiceImp(PatientRepository patientRepository, WebClient.Builder webClient,
                             DocumentRepository documentsRepository) {
        this.patientRepository = patientRepository;
        this.webClient = webClient;
        this.documentsRepository = documentsRepository;
    }
    @Override
    public void create(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient, long id) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        for (Field field : Patient.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("id")) { // Exclude the id field
                    Object newValue = field.get(patient);
                    if (newValue != null) {
                        field.set(existingPatient, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        return patientRepository.save(existingPatient);
    }

    @Override
    public void delete(long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public List<PatientDTO> findAll() {
        List<Patient> patients = patientRepository.findAll();
//        return new ArrayList<>(patients);
        return patients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO getAppointments(long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        List<Map<String, Object>> appointments;
        try {
            appointments = webClient.build()
                    .get()
                    .uri("http://appointment-service/api/appointment/patient/" + patientId)
                    .retrieve()
                    .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .collectList()
                    .block();
        } catch (Exception e) {
            logger.error("Error fetching appointments for patientId {}: {}", patientId, e.getMessage(), e);
            throw new BadRequestException("Failed to fetch appointments: " + e.getMessage());
        }
        List<Documents> documents = documentsRepository.findByPatientId(patientId);
        List<DocumentDTO> documentDTOs = documents.stream()
                .map(doc -> convertToDTODocs(doc))
                .collect(Collectors.toList());

        PatientDTO patientDTO = convertToDTO(patient);
        patientDTO.setAppointments(appointments);
        patientDTO.setDocuments(documentDTOs);
        return patientDTO;
    }

    @Override
    public PatientDTO getById(long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        return convertToDTO(patient);
    }






    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO patientDTO = new PatientDTO();
        BeanUtils.copyProperties(patient, patientDTO);
        return patientDTO;
    }
    private DocumentDTO convertToDTODocs(Documents document) {
        DocumentDTO documentDTO = new DocumentDTO();
        BeanUtils.copyProperties(document, documentDTO);
        return documentDTO;
    }

}
