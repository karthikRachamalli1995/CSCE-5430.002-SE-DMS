package com.softwareengineering.project.dms.web.controller;

import com.softwareengineering.project.dms.model.Document;
import com.softwareengineering.project.dms.service.DocumentService;
import com.softwareengineering.project.dms.web.resource.DocumentSearchResource;
import com.softwareengineering.project.dms.web.resource.DocumentSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/dms/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /**
     * API to Upload the document
     *
     * @param metadata String
     * @param file MultipartFile
     * @return httpStatus HttpStatus
     */
    @PostMapping(value = "/store", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> storeDocument(@RequestPart("metadata") String metadata, @RequestPart("file") MultipartFile file) {
        documentService.storeDocument(file, metadata);
        return new ResponseEntity<>(OK);
    }

    /**
     * API to Update the document
     * @param fileId String
     * @param file MultipartFile
     * @return httpStatus HttpStatus
     */
    @PutMapping(value = "/{fileid}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> updateDocument(@PathVariable("fileid") String fileId, @RequestPart("file") MultipartFile file) {
        documentService.updateDocument(fileId, file);
        return new ResponseEntity<>(OK);
    }

    /**
     * API to update the metadata of an existing document.
     * @param fileId String
     * @param metadata String
     * @return httpStatus HttpStatus
     */
    @PutMapping(value = "/{fileid}/metadata", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> updateMetadata(@PathVariable("fileid") String fileId, @RequestPart("metadata") String metadata) {
        documentService.updateMetadata(fileId, metadata);
        return new ResponseEntity<>(OK);
    }

    /**
     * API to fetch the document by fileId.
     * @param fileId String
     * @return Resource resource
     */
    @GetMapping("/{fileid}")
    public ResponseEntity<Resource> fetchDocument(@PathVariable("fileid") String fileId) {
        Document document = documentService.fetchDocument(fileId);
        // Preparing CONTENT_DISPOSITION headers and content type to file type and body with the byte array of the file.
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(new ByteArrayResource(document.getData()));
    }

    /**
     * API to delete the document for a given Id.
     * @param fileId String
     * @return httpStatus HttpStatus
     */
    @DeleteMapping("/{fileid}")
    public ResponseEntity<HttpStatus> deleteDocument(@PathVariable("fileid") String fileId) {
        documentService.deleteDocument(fileId);
        return new ResponseEntity<>(OK);
    }

    /**
     * API to search the documents with given document search criteria.
     * @param resource DocumentSearchResource
     * @return response DocumentSearchResponse
     */
    @PostMapping("/search")
    public ResponseEntity<DocumentSearchResponse> searchDocuments(@RequestBody DocumentSearchResource resource) {
        return new ResponseEntity<>(documentService.searchDocuments(resource), OK);
    }

}
