package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// DEPENDENCY INJECTION
// Spring Boot starts -> Scans for classes with @Component/@Service/@Repository and creates beans for them
// @AllArgsConstructors/@RequiredArgsConstructor inject dependencies
// ActivityController gets ActivityService via constructor injection
@RestController // Automatically converts Java objects to JSON for responses
@RequestMapping("/api/activities") // every endpoint starts with /api/activities
@AllArgsConstructor // Spring uses this constructor for dependency injection
public class ActivityController {

    // If a bean of type ActivityService is in the application context, creates an instance of ActivityController
    // since ActivityController depends on ActivityService having a bean
    // Spring then uses generated constructor to inject the ActivityService automatically into the new instance
    private ActivityService activityService;

    // Lombok generates this constructor from @AllArgsConstructor during compile time
//    public ActivityController (ActivityService activityService) {
//        this.activityService = activityService;
//    }

    // Controller hands request to the service. THe service fetches Activity entities from MongoDB,
    // then the service converts the activity into an ActivityResponse and controller sends it back to client

    // Frontend data will become an ActivityRequest object with @RequestBody
    // ActivityResponse will be a DTO that returns the user's activity from the database
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request, @RequestHeader("X-User-ID") String userId) {
        if (userId != null) {
            request.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

    // Needs to be a List<ActivityResponse> since a user can have multiple activities
    // THe frontend will have a header of "X-User-ID", then populates it into userId
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

    // Gets specific activity by its id
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable String activityId) {
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}
