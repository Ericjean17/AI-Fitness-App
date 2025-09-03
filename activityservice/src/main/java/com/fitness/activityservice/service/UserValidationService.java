package com.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
@RequiredArgsConstructor // sees userServiceWebClient as a required dependency
public class UserValidationService {
    private final WebClient userServiceWebClient; // bean from WebClientConfig is injected here

    // ActivityService calls smth like UserValidationService.validateUser("1")
    // Inside this method, the userServiceWebClient is used to send a GET request to http://USER-SERVICE/api/users/1/validate
    // @LoadBalanced in WebClientConfig, USER-SERVICE is resolved on Eureka instead of DNS so it gets localhost:8081
    // WebClient sends the request to that instance and a boolean is returned
    // This is basically the Activity microservices calling an API endpoint by the User microservice and it goes through Eureka
    // and load balancing instead of DNS or a hardcoded port
    public boolean validateUser(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        try {
            return Boolean.TRUE.equals(userServiceWebClient.get() //creates get request
                    .uri("/api/users/{userId}/validate", userId)//fill uri with userId value
                    .retrieve()//send request + waits for a response back
                    .bodyToMono(Boolean.class) //expect body as boolean
                    .block());//wait until response arrives
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("User Not Found: " + userId);
            }
            else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Invalid Request: " + userId);
            }
        }
        return false;
    }
}
