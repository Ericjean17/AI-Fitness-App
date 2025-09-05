package com.fitness.activityservice.service;

import com.fitness.activityservice.ActivityRepository;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Generates a constructor with required args that are only final and marked with @NonNull
@Slf4j
public class ActivityService {

    // These dependencies can't be changed after injection bc it is immutable
    // We use RequiredArgsConstructor since ActivityService needs these dependencies to do its job
    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate; // Helper class that simplifies synchronous RabbitMQ access (sending/receiving messages)

    @Value("${rabbitmq.exchange.name}") // injects values from application.yml into these fields, syntax ${} reads from config files
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // Converts the ActivityRequest sent from the controller and builds the Activity entity.
    // Saves the entity into the database, and returns the ActivityResponse
    public ActivityResponse trackActivity(ActivityRequest request) {

        //Reaches out to User Service via HTTP to check if user exists before creating activity
        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if (!isValidUser) {
            throw new RuntimeException("Invalid User" + request.getUserId());
        }

        // Uses builder pattern for readable Activity object construction
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        // Uses repository pattern which abstracts database operations
        Activity savedActivity = activityRepository.save(activity);

        //Publish to RabbitMQ for AI processing
        try {
            // Asynchronous processing -> don't wait for Gemini to respond since it isn't critical to saving activity
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        } catch (Exception e){
            log.error("Failed to publish activity to RabbitMQ: ", e);
        }
        return mapToResponse(savedActivity);
    }

    // Collect every Activity with userId from the database and map it to List<ActivityResponse>
    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);

        // Convert each Activity object into an ActivityResponse
        return activities.stream() // .stream() turns Collection<Activity> into a stream
                .map(this::mapToResponse) // Calls mapToResponse() for every activity in the list -> Stream<ActivityResponse>
                .collect(Collectors.toList()); // Collect the stream back into a List -> List<ActivityResponse>
    }

    // Return an ActivityResponse with activityId
    public ActivityResponse getActivityById(String activityId) {
        // findById returns Optional meaning the activity might not exist so need to throw an exception
        return activityRepository.findById(activityId)
                .map(this::mapToResponse) // .map() returns an Optional (need an orElse statement)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));
    }

    // Convert Activity to ActivityResponse
    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }
}
