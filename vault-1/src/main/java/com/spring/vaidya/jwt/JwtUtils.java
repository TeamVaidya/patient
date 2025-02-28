package com.spring.vaidya.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import com.spring.vaidya.service.HashiCorpApiService;
import com.spring.vaidya.service.TokenService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for JWT (JSON Web Token) operations, including token generation,
 * validation, and extracting user details. This class retrieves the JWT secret
 * from HashiCorp Vault at application startup.
 */
@Component
public class JwtUtils implements CommandLineRunner {

    private String jwtSecret; // Secret key for signing JWT tokens
    private final int jwtExpirationMs = 86400000; // Token expiration time (1 day)

    @Autowired
    private VaultTemplate vaultTemplate; // Vault template for secure storage access

    @Autowired
    private TokenService tokenService; // Service to handle authentication token refresh

    @Autowired
    private HashiCorpApiService hashicorpApiService; // Service to interact with HashiCorp Vault

    /**
     * Generates a JWT token for a given username.
     *
     * @param username The username for which the token is generated.
     * @return A signed JWT token as a string.
     */
    public String generateJwtToken(String username) {
        try {
            Map<String, Object> claims = new HashMap<>(); // Can be used to store additional claims if needed
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username) // Set username as the token subject
                    .setIssuedAt(new Date()) // Set issue time
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Set expiration time
                    .signWith(SignatureAlgorithm.HS512, jwtSecret) // Sign with secret key
                    .compact();
        } catch (Exception e) {
            System.err.println("Error generating JWT token: " + e.getMessage());
            return null; // Return null if token generation fails
        }
    }

    /**
     * Validates a given JWT token.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token); // Parse and validate token
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token has expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Unsupported JWT token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Malformed JWT token: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    /**
     * Extracts the username (subject) from a given JWT token.
     *
     * @param token The JWT token.
     * @return The username if extraction is successful, null otherwise.
     */
    public String getUsernameFromJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException e) {
            System.err.println("Error extracting username from JWT: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetches the JWT secret key from HashiCorp Vault at application startup.
     * This method runs automatically when the application starts.
     *
     * @param strings Command-line arguments (not used).
     */
    @Override
    public void run(String... strings) {
        try {
            tokenService.refreshAccessToken(); // Refresh the access token before fetching the JWT secret
            jwtSecret = hashicorpApiService.callHashiCorpApi(); // Retrieve secret from HashiCorp Vault
            System.out.println("JWT Secret successfully retrieved.");
        } catch (Exception e) {
            System.err.println("Error fetching JWT secret from Vault: " + e.getMessage());
        }
    }
}
