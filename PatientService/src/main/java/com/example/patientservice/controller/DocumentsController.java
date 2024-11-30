package com.example.patientservice.controller;

import com.example.patientservice.dto.DocumentDTO;
import com.example.patientservice.model.Documents;
import com.example.patientservice.service.DocumentService;
import com.example.patientservice.service.DocumentServiceImp;
import com.example.patientservice.service.PatientServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Documents", description = "the Document Api")
@RestController
@RequestMapping("/api/documents")
public class DocumentsController {
    private final DocumentServiceImp documentService;

    public DocumentsController(DocumentServiceImp documentService) {
        this.documentService = documentService;
    }

    @Operation(
            summary = "Upload patient Docs",
            description = "Upload patient Docs in the database")
    @PostMapping("/{id}")
    public ResponseEntity<?> uploadDoc(@PathVariable long id, MultipartFile file, DocumentDTO documentDTO) throws IOException {
        try {
            DocumentDTO document = documentService.uploadDoc(id,file,documentDTO);
            return new ResponseEntity<>(document, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload Doc: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
