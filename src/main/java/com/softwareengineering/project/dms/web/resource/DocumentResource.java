package com.softwareengineering.project.dms.web.resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentResource {
    private String fileId;
    private String fileName;
    private String fileType;
    private String metadata;
}
