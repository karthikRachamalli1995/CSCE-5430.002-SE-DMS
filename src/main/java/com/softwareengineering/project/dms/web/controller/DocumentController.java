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

    @PostMapping(value = "/store", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> storeDocument(@RequestPart("metadata") String metadata, @RequestPart("file") MultipartFile file) {
        documentService.storeDocument(file, metadata);
        return new ResponseEntity<>(OK);
    }

    @PutMapping(value = "/{fileid}/metadata", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> updateMetadata(@PathVariable("fileid") String fileId, @RequestPart("metadata") String metadata) {
        documentService.updateMetadata(fileId, metadata);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/{fileid}")
    public ResponseEntity<Resource> fetchDocument(@PathVariable("fileid") String fileId) {
        Document document = documentService.fetchDocument(fileId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(document.getFileType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"").body(new ByteArrayResource(document.getData()));
    }

    @DeleteMapping("/{fileid}")
    public ResponseEntity<HttpStatus> deleteDocument(@PathVariable("fileid") String fileId) {
        documentService.deleteDocument(fileId);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("/search")
    public ResponseEntity<DocumentSearchResponse> searchDocuments(@RequestBody DocumentSearchResource resource) {
        return new ResponseEntity<>(documentService.searchDocuments(resource), OK);
    }

}
