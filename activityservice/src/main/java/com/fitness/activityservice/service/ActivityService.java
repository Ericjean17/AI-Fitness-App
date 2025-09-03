package com.fitness.activityservice.service;

import com.fitness.activityservice.ActivityRepository;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Generates a constructor with required args that are only final and marked with @NonNull
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;

    // Converts the ActivityRequest sent from the controller and builds the Activity entity.
    // Saves the entity into the database, and returns the ActivityResponse
    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if (!isValidUser) {
            throw new RuntimeException("Invalid User" + request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
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
