package com.example.patientservice.service;

import com.example.patientservice.dto.DocumentDTO;
import com.example.patientservice.model.Documents;
import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.DocumentRepository;
import com.example.patientservice.repository.PatientRepository;
import com.example.patientservice.utils.uploadFile;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentServiceImp implements DocumentService {

    private final DocumentRepository documentsRepository;
    private final PatientRepository patientRepository;
    private final uploadFile uploadFile;

    public DocumentServiceImp(DocumentRepository documentsRepository, PatientRepository patientRepository, uploadFile uploadFile) {
        this.documentsRepository = documentsRepository;
        this.patientRepository = patientRepository;
        this.uploadFile = uploadFile;
    }

    @Override
    public DocumentDTO uploadDoc(long patientId, MultipartFile file, DocumentDTO documentDTO) throws IOException {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        String fileUrl = uploadFile.upload(file);

        Documents document = new Documents();
        document.setTitle(documentDTO.getTitle());
        document.setAttachement(fileUrl);
        document.setPatient(patient);
        documentsRepository.save(document);
        return convertToDTO(document);
    }

    private DocumentDTO convertToDTO(Documents document) {
        DocumentDTO documentDTO = new DocumentDTO();
        BeanUtils.copyProperties(document, documentDTO);
        return documentDTO;
    }
}