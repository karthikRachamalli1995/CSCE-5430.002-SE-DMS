package com.softwareengineering.project.dms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenValidator {

    public boolean validateToken(String header, String role) {
        // Check for the Bearer token or not
        if (header == null || !header.startsWith("Bearer ")) {
            return false;
        }

        // Remove the Bearer key
        String token = header.substring(7);
        try {
            // Parse the JWT token
            Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
            // Fetch the roles
            List<String> roles = claims.get("roles", List.class);
            // Get the Expiry Date
            Date expirationDate = claims.getExpiration();
            // Checking the expiry date and roles.
            return expirationDate.after(new Date()) && roles.contains(role);
        } catch (Exception e) {
            return false;
        }
    }
}
