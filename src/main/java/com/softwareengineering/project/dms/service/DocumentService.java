package com.softwareengineering.project.dms.service;

import com.softwareengineering.project.dms.model.Document;
import com.softwareengineering.project.dms.web.resource.DocumentSearchResource;
import com.softwareengineering.project.dms.web.resource.DocumentSearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface DocumentService {
    void storeDocument(MultipartFile file, String metadata);

    void updateDocument(String fileId, MultipartFile file);

    void updateMetadata(String fileId, String metadata);

    Document fetchDocument(String id);

    void deleteDocument(String fileId);

    DocumentSearchResponse searchDocuments(DocumentSearchResource resource);
}
