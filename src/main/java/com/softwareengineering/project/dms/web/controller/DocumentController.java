package com.softwareengineering.project.dms.web.controller;

import com.softwareengineering.project.dms.model.Document;
import com.softwareengineering.project.dms.security.JwtTokenValidator;
import com.softwareengineering.project.dms.service.DocumentService;
import com.softwareengineering.project.dms.web.resource.DocumentSearchResource;
import com.softwareengineering.project.dms.web.resource.DocumentSearchResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import static com.softwareengineering.project.dms.util.DataConstants.AUTH_ROLES;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/dms/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    /**
     * API to Upload the document
     *
     * @param metadata String
     * @param file     MultipartFile
     * @return httpStatus HttpStatus
     */
    @PostMapping(value = "/store", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> storeDocument(@RequestHeader("Authorization") String authorization,
                                                    @RequestPart("metadata") String metadata,
                                                    @RequestPart("file") MultipartFile file) {
        // Validating the token
        if (jwtTokenValidator.validateToken(authorization, AUTH_ROLES.get("UPLOAD"))) {
            documentService.storeDocument(file, metadata);
            return new ResponseEntity<>(OK);
        } else {
            // Return 401 Unauthorized
            return new ResponseEntity<>(UNAUTHORIZED);
        }
    }

    /**
     * API to Update the document
     *
     * @param fileId String
     * @param file   MultipartFile
     * @return httpStatus HttpStatus
     */
    @PutMapping(value = "/{fileid}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> updateDocument(@RequestHeader("Authorization") String authorization,
                                                     @PathVariable("fileid") String fileId, @RequestPart("file") MultipartFile file) {
        // Validating the token
        if (jwtTokenValidator.validateToken(authorization, AUTH_ROLES.get("UPDATE"))) {
            documentService.updateDocument(fileId, file);
            return new ResponseEntity<>(OK);
        } else {
            // Return 401 Unauthorized
            return new ResponseEntity<>(UNAUTHORIZED);
        }
    }

    /**
     * API to update the metadata of an existing document.
     *
     * @param fileId   String
     * @param metadata String
     * @return httpStatus HttpStatus
     */
    @PutMapping(value = "/{fileid}/metadata", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<HttpStatus> updateMetadata(@RequestHeader("Authorization") String authorization,
                                                     @PathVariable("fileid") String fileId, @RequestPart("metadata") String metadata) {
        // Validating the token
        if (jwtTokenValidator.validateToken(authorization, AUTH_ROLES.get("UPDATE"))) {
            documentService.updateMetadata(fileId, metadata);
            return new ResponseEntity<>(OK);
        } else {
            // Return 401 Unauthorized
            return new ResponseEntity<>(UNAUTHORIZED);
        }
    }

    /**
     * API to fetch the document by fileId.
     *
     * @param fileId String
     * @return Resource resource
     */
    @GetMapping("/{fileid}")
    public ResponseEntity<Resource> fetchDocument(@RequestHeader("Authorization") String authorization,
                                                  @PathVariable("fileid") String fileId) {
        // Validating the token
        if (jwtTokenValidator.validateToken(authorization, AUTH_ROLES.get("GET"))) {
            Document document = documentService.fetchDocument(fileId);
            // Preparing CONTENT_DISPOSITION headers and content type to file type and body with the byte array of the file.
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(document.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                    .body(new ByteArrayResource(document.getData()));
        } else {
            // Return 401 Unauthorized
            return new ResponseEntity<>(UNAUTHORIZED);
        }
    }

    /**
     * API to delete the document for a given Id.
     *
     * @param fileId String
     * @return httpStatus HttpStatus
     */
    @DeleteMapping("/{fileid}")
    public ResponseEntity<HttpStatus> deleteDocument(@RequestHeader("Authorization") String authorization,
                                                     @PathVariable("fileid") String fileId) {
        // Validating the token
        if (jwtTokenValidator.validateToken(authorization, AUTH_ROLES.get("DELETE"))) {
            documentService.deleteDocument(fileId);
            return new ResponseEntity<>(OK);
        } else {
            // Return 401 Unauthorized
            return new ResponseEntity<>(UNAUTHORIZED);
        }
    }

    /**
     * API to search the documents with given document search criteria.
     *
     * @param resource DocumentSearchResource
     * @return response DocumentSearchResponse
     */
    @PostMapping("/search")
    public ResponseEntity<DocumentSearchResponse> searchDocuments(@RequestHeader("Authorization") String authorization,
                                                                  @RequestBody DocumentSearchResource resource) {
        // Validating the token
        if (jwtTokenValidator.validateToken(authorization, AUTH_ROLES.get("SEARCH"))) {
            return new ResponseEntity<>(documentService.searchDocuments(resource), OK);
        } else {
            // Return 401 Unauthorized
            return new ResponseEntity<>(UNAUTHORIZED);
        }
    }

    /**
     * API to fetch the token
     *
     * @param roles List<String>
     * @param milliSeconds long
     * @return String
     */
    @PostMapping("/getToken")
    public String getToken(@RequestBody List<String> roles, @RequestParam("expiry") long milliSeconds) {
        // calculate expiry date millis
        Date expiryDate = new Date(new Date().getTime() + milliSeconds);
        // returns a JWT token.
        return Jwts.builder()
                .setSubject("karthik") // sets the subject
                .setExpiration(expiryDate) // sets the expiry
                .claim("roles", roles) // sets the roles
                .signWith(SignatureAlgorithm.HS256, "secret") // sets the secret
                .compact();
    }

}
