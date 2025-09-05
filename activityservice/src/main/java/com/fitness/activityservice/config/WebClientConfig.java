package com.fitness.activityservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// Configures how a service communicates with other microservices over HTTP
@Configuration // Defines Spring beans and add them to application context
public class WebClientConfig {

    // Creates a load-balanced WebClient builder that understands Eureka service names
    @Bean
    @LoadBalanced //allows web client to use service names via Eureka instead of URL (localhost)
    public WebClient.Builder webClientBuilder() { //WebClient.Builder is a factory for creating WebClient instances (non-blocking HTTP client)
        return WebClient.builder();
    }

    // Creates a ready-to-use WebClient preconfigured for talking to the user service and puts it into application context on startup
    @Bean
    public WebClient userServiceWebClient(WebClient.Builder webClientBuilder) {
        // @LoadBalanced made Eureka look for the URL associated with user-service instead of the URL itself
        // If user-service moves to another port, server, or has multiple instances, Eureka + load balancer handles it
        return webClientBuilder.baseUrl("http://user-service")
                .build();
    }
}
