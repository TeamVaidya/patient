package com.spring.vaidya.service;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;

@Service
public class TokenService {

    // TODO: Move sensitive credentials to application properties or environment variables
    private static final String CLIENT_ID = "rRn0jXxVGd3qLqQfLCJn39xTa0hM2hAY"; // Your client ID
    private static final String CLIENT_SECRET = "_KMaWsYp6VRHDA33y8rd2DLBmiplvozitXCbk6vYyMNIsDxTglODs8o0xTlVd77O"; // Your client secret
    private static final String TOKEN_URL = "https://auth.idp.hashicorp.com/oauth2/token";

    private final RestTemplate restTemplate = new RestTemplate();

    // Store the token in memory using AtomicReference to safely handle updates
    private final AtomicReference<String> accessToken = new AtomicReference<>("");

    /**
     * Fetch and update the access token from HashiCorp Identity Provider.
     */
    public void refreshAccessToken() {
        try {
            // Set HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Request body for OAuth2 token request
            String requestBody = "client_id=" + CLIENT_ID +
                                 "&client_secret=" + CLIENT_SECRET +
                                 "&grant_type=client_credentials" +
                                 "&audience=https://api.hashicorp.cloud";

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Send the POST request to obtain a new token
            ResponseEntity<Map> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, Map.class);

            // Validate and store the new token
            if (response.getBody() != null && response.getBody().containsKey("access_token")) {
                String token = response.getBody().get("access_token").toString();
                accessToken.set(token); // Store the token safely

                // Logging for debugging (Consider using a logger instead of System.out)
                System.out.println("🔄 Token refreshed successfully! Current Token: " + token);
            } else {
                System.err.println("❌ Failed to refresh token! Response: " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("❌ Error refreshing token: " + e.getMessage());
        }
    }

    /**
     * Scheduler to automatically refresh the token every 55 minutes.
     * (Token expiration is typically 1 hour, so we refresh before expiration)
     */
    @Scheduled(fixedRate = 55 * 60 * 1000) // 55 minutes in milliseconds
    public void scheduledTokenRefresh() {
        refreshAccessToken();
    }

    /**
     * Retrieves the most recently fetched access token.
     * @return The current access token.
     */
    public String getAccessToken() {
        return accessToken.get();
    }
}
