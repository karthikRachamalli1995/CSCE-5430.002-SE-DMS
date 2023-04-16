package com.softwareengineering.project.dms.repository;

import com.softwareengineering.project.dms.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    // Native Query to operate full text search on meta_data column.
    @Query(value = "select * from Document where dbms_lob.instr(meta_data, :fullTextSearchString) > 0", nativeQuery = true)
    List<Document> searchMetadata(String fullTextSearchString);
}
