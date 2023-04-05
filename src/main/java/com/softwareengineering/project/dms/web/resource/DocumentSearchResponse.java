package com.softwareengineering.project.dms.web.resource;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocumentSearchResponse {
    List<DocumentResource> results;
}
