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

    /**
     * Store Document method takes file and metadata as params to store the document in the database.
     * @param file MultipartFile
     * @param metadata String
     */
    @Override
    public void storeDocument(MultipartFile file, String metadata) {
        try {
            // Building Document object with FileName, FileType, Data, and Metadata
            Document document = new Document();
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setData(file.getBytes()); // converting the file into byte array.
            document.setMetadata(metadata);
            // save the document to database.
            documentRepository.save(document);
        } catch (Exception ex) {
            // Print error to the console.
            System.err.println("Failed to Upload the file, due to: " + ex);
        }
    }

    /**
     * Update Document method takes file id and file to update the existing document with the file Id.
     * @param fileId String
     * @param file MultipartFile
     */
    @Override
    public void updateDocument(String fileId, MultipartFile file) {
        try {
            // Using "findById" to fetch the document based on file id.
            Optional<Document> documentOptional = documentRepository.findById(fileId);
            if (documentOptional.isPresent()) {
                // if the document is present, update all the properties of the document except metadata
                Document document = documentOptional.get();
                document.setFileName(file.getOriginalFilename());
                document.setFileType(file.getContentType());
                document.setData(file.getBytes());
                // Update the document to the database
                documentRepository.save(document);
            }
        } catch (Exception ex) {
            // Print error to the console.
            String errorMessage = String.format("Failed to update the file with id: %s", fileId);
            System.err.println(errorMessage + ", due to: " + ex);
        }
    }

    /**
     * UpdateMetadata method takes fileId and metadata as params to update an existing metadata.
     * @param fileId String
     * @param metadata String
     */
    @Override
    public void updateMetadata(String fileId, String metadata) {
        try {
            // Using "findById" to fetch the document based on file id.
            Optional<Document> documentOptional = documentRepository.findById(fileId);
            if (documentOptional.isPresent()) {
                // if the document is present, update only metadata
                Document document = documentOptional.get();
                document.setMetadata(metadata);
                // Update the document to the database
                documentRepository.save(document);
            }
        } catch (Exception ex) {
            // Print error to the console.
            String errorMessage = String.format("Failed to update metedata for the file with id: %s", fileId);
            System.err.println(errorMessage + ", due to: " + ex);
        }
    }

    /**
     * fetchDocument method will fetch the document, corresponding to given file Id.
     * @param fileId String
     * @return document Document
     */
    @Override
    public Document fetchDocument(String fileId) {
        // Using "findById" to fetch the document based on file id.
        Optional<Document> documentOptional = documentRepository.findById(fileId);
        // Return if the document is present if not return null.
        return documentOptional.orElse(null);
    }

    /**
     * deleteDocument method will delete the document with the given id.
     * @param fileId String
     */
    @Override
    public void deleteDocument(String fileId) {
        // Using "deleteById" to delete the document based on file id.
        documentRepository.deleteById(fileId);
    }

    /**
     * searchDocuments merthod will search the document with given search criteria.
     * @param resource DocumentSearchResource
     * @return response DocumentSearchResponse
     */
    @Override
    public DocumentSearchResponse searchDocuments(DocumentSearchResource resource) {
        // "searchMetadata" method in the repository runs the native query to perform full text search.
        List<Document> documents = documentRepository.searchMetadata(resource.getSearchText());
        // We stream the documents using JAVA 8 Stream API and build only the required fields for the result.
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
