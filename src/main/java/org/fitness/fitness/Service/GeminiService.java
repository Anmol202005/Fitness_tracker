package org.fitness.fitness.service;

import java.util.List;

import org.fitness.fitness.Model.DTO.GeminiRequest;
import org.fitness.fitness.Model.DTO.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey; // Store the API key in application.properties for security.

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getGeneratedText(String inputText) {
        // Prepare the request body
        GeminiRequest request = new GeminiRequest();
        GeminiRequest.Content content = new GeminiRequest.Content();
        GeminiRequest.Content.Part part = new GeminiRequest.Content.Part();
        part.setText(inputText);
        content.setParts(List.of(part));
        request.setContents(List.of(content));

        // Prepare the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Prepare the HTTP entity with body and headers
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        // Make the HTTP POST request
        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                GEMINI_API_URL + "?key=" + geminiApiKey,
                HttpMethod.POST,
                entity,
                GeminiResponse.class
        );

        // Extract the 'text' field from the response
        if (response.getBody() != null && !response.getBody().getCandidates().isEmpty()) {
            return response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();
        }

        // In case no valid response or candidates are found
        return "No content generated.";
    }
}
