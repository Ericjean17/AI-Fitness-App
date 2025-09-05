package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    // Run rabbitmq with docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
    // Docker run starts container, -p 5672:5672 exposes port 5672 in AMQP protocol so Spring Boot can connect to it
    // -p 15672:15672 exposes port 15672 in HTTP for RabbitMQ management UI
    // rabbitmq:4-management creates image with RabbitMQ plus management plugin

    private final ActivityAIService aiService;
    private final RecommendationRepository recommendationRepository;

    // Tells Spring to set up a message listener for RabbitMQ queue named activity.queue
    // when a new message lands in that queue in which the message has exchange name and routing key in convertAndSend()
    // When a message arrives, Spring deserializes the incoming JSON into Activity object
    // Same queue which activity service posted the data
    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity) {
        log.info("Received activity for processing: {}", activity.getId());
        try {
            Recommendation recommendation = aiService.generateRecommendation(activity);
            log.info("Generated Recommendation: {}", recommendation);
            recommendationRepository.save(recommendation);
        } catch (Exception e) {
            log.error("Failed to process activity {}: {}", activity.getId(), e.getMessage());
            // Don't rethrow - this prevents infinite retries
        }
    }
}
