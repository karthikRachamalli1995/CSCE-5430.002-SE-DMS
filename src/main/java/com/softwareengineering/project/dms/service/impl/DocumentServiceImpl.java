package com.softwareengineering.project.dms.service.impl;

import com.softwareengineering.project.dms.model.Document;
import com.softwareengineering.project.dms.repository.DocumentRepository;
import com.softwareengineering.project.dms.service.DocumentService;
import com.softwareengineering.project.dms.web.resource.DocumentResource;
import com.softwareengineering.project.dms.web.resource.DocumentSearchResource;
import com.softwareengineering.project.dms.web.resource.DocumentSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public void storeDocument(MultipartFile file, String metadata) {
        try {
            Document document = new Document();
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setData(file.getBytes());
            document.setMetadata(metadata);
            // here upload the metadata to elastic
            documentRepository.save(document);
        } catch (Exception ex) {
            System.err.println("Failed to Upload the file, due to: " + ex);
        }
    }

    @Override
    public void updateDocument(String fileId, MultipartFile file) {
        try {
            Optional<Document> documentOptional = documentRepository.findById(fileId);
            if (documentOptional.isPresent()) {
                Document document = documentOptional.get();
                document.setFileName(file.getOriginalFilename());
                document.setFileType(file.getContentType());
                document.setData(file.getBytes());
                documentRepository.save(document);
            }
        } catch (Exception ex) {
            String errorMessage = String.format("Failed to update the file with id: %s", fileId);
            System.err.println(errorMessage + ", due to: " + ex);
        }
    }

    @Override
    public void updateMetadata(String fileId, String metadata) {
        try {
            Optional<Document> documentOptional = documentRepository.findById(fileId);
            if (documentOptional.isPresent()) {
                Document document = documentOptional.get();
                document.setMetadata(metadata);
                documentRepository.save(document);
            }
        } catch (Exception ex) {
            String errorMessage = String.format("Failed to update metedata for the file with id: %s", fileId);
            System.err.println(errorMessage + ", due to: " + ex);
        }
    }

    @Override
    public Document fetchDocument(String fileId) {
        Optional<Document> documentOptional = documentRepository.findById(fileId);
        return documentOptional.orElse(null);
    }

    @Override
    public void deleteDocument(String fileId) {
        documentRepository.deleteById(fileId);
    }

    @Override
    public DocumentSearchResponse searchDocuments(DocumentSearchResource resource) {
        List<Document> documents = documentRepository.searchMetadata(resource.getSearchText());
        List<DocumentResource> documentResources = documents.stream()
                .map(document ->
                        DocumentResource.builder()
                                .fileId(document.getId())
                                .fileName(document.getFileName())
                                .fileType(document.getFileType())
                                .metadata(document.getMetadata())
                                .build()
                ).collect(Collectors.toList());
        return DocumentSearchResponse.builder().results(documentResources).build();
    }
}
