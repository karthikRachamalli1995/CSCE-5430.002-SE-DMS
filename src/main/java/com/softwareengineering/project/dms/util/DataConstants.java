package com.softwareengineering.project.dms.util;

import java.util.HashMap;

public class DataConstants {

    public static final HashMap<String, String> AUTH_ROLES = new HashMap<>();

    static {
        AUTH_ROLES.put("UPLOAD", "UPLOAD_ROLE");
        AUTH_ROLES.put("UPDATE", "UPDATE_ROLE");
        AUTH_ROLES.put("GET", "GET_ROLE");
        AUTH_ROLES.put("DELETE", "DELETE_ROLE");
        AUTH_ROLES.put("SEARCH", "SEARCH_ROLE");
    }
}
