package com.example.patientservice.service;

import com.example.patientservice.dto.DocumentDTO;
import com.example.patientservice.model.Documents;
import com.example.patientservice.model.Patient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface DocumentService {
    DocumentDTO uploadDoc(long patientId, MultipartFile file, DocumentDTO documentDTO) throws IOException;
    DocumentDTO deleteDoc(long documentId) throws IOException;
}
