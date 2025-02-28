package com.spring.vaidya.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Service to interact with HashiCorp Vault API.
 * Retrieves secrets using an access token obtained from the TokenService.
 */
@Service
public class HashiCorpApiService {

	
	@Autowired
    private final TokenService tokenService;

    @Value("${hashicorp.apiUrl}") // API URL is injected from application properties
    private String apiUrl;

    /**
     * Constructor for HashiCorpApiService.
     * @param tokenService Service that manages access tokens for authentication.
     */
    public HashiCorpApiService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Calls the HashiCorp Vault API to retrieve a secret value.
     * @return The extracted secret value as a String.
     */
    public String callHashiCorpApi() {
        // Fetch access token from TokenService
        String accessToken = tokenService.getAccessToken();
        System.out.println("Using Access Token: " + accessToken); // Debugging purpose

        // Define the API endpoint URL
        String url = apiUrl;

        // Create a RestTemplate instance for making API requests
        RestTemplate restTemplate = new RestTemplate();

        // Set up HTTP headers, including the Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Create an HTTP entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make an API call using RestTemplate and retrieve the response
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Check if the response is successful
        if (response.getStatusCode() == HttpStatus.OK) {
            String secretValue = extractSecretValue(response.getBody()); // Extract the secret value
            System.out.println("Stored Secret Value: " + secretValue); // Debugging purpose
            return secretValue;
        } else {
            throw new RuntimeException("Failed to call HashiCorp API. HTTP Status: " + response.getStatusCode());
        }
    }

    /**
     * Extracts the secret value from the JSON response returned by HashiCorp Vault API.
     * @param responseBody JSON response body as a String.
     * @return Extracted secret value as a String.
     */
    private String extractSecretValue(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Navigate JSON structure to extract the required secret value
            return rootNode.path("secrets").get(0).path("static_version").path("value").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON response", e);
        }
    }
}
