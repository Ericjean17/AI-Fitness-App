package com.fitness.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor // sees userServiceWebClient as a required dependency
public class UserService {
    // Spring's HTTP client
    private final WebClient userServiceWebClient; // bean from WebClientConfig is injected here

    // ActivityService calls smth like UserValidationService.validateUser("1")
    // Inside this method, the userServiceWebClient is used to send a GET request to http://USER-SERVICE/api/users/1/validate
    // @LoadBalanced in WebClientConfig, USER-SERVICE is resolved on Eureka instead of DNS so it gets localhost:8081
    // WebClient sends the request to that instance and a boolean is returned
    // This is basically the Activity microservices calling an API endpoint by the User microservice and it goes through Eureka
    // and load balancing instead of DNS or a hardcoded port
    public Mono<Boolean> validateUser(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return userServiceWebClient.get() //creates get request
            .uri("/api/users/{userId}/validate", userId)//fill uri with userId value
            .retrieve()//send request and returns boolean response back
            .bodyToMono(Boolean.class) //expect body as boolean
            .onErrorResume(WebClientResponseException.class, e -> {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                    return Mono.error(new RuntimeException("User Not Found " + userId));
                else if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                    return Mono.error(new RuntimeException("Invalid Request " + userId));
                return Mono.error(new RuntimeException("Unexpected error " + e.getMessage()));
            });

    }

    public Mono<UserResponse> registerUser(RegisterRequest request) {
        log.info("Calling User Registration API for email: {}", request.getEmail());
        return userServiceWebClient.post() //creates get request
                .uri("/api/users/register")//fill uri with userId value
                .bodyValue(request) // what goes into the body
                .retrieve()//send request and returns UserResponse response back
                .bodyToMono(UserResponse.class) //expect body as UserResponse
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                        return Mono.error(new RuntimeException("Bad Request: " + e.getMessage()));
                    else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                        return Mono.error(new RuntimeException("Internal Server Error " + e.getMessage()));
                    return Mono.error(new RuntimeException("Unexpected error " + e.getMessage()));
                });
    }
}
