package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    // Instead of having a WebClientConfig class, just put everything in constructor
    /*
    @Configuration
    public class WebClientConfig {
        @Bean
        public WebClient webClient() {
            return WebClient.builder()
                    .baseUrl("https://api.example.com")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
        }
    }
    private final WebClient webClient;
    public ApiService(WebClient webClient) {
            this.webClient = webClient;
    }
     */
    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // Look at the structure of getting the question
    public String getAnswer(String question) {
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                            Map.of("text", question)
                        })
                }
        );

        // Make post request to Gemini API with header of content-type of json
        // In the body, we send the question, and we expect a String response
        String response = webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }


}
