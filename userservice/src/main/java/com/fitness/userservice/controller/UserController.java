package com.fitness.userservice.controller;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor // Generates a constructor for every field, no need to autowire userService
public class UserController {

    //@Autowired
    private UserService userService;

    // ResponseEntity gives HTTP statuses, @PathVariable gets the URL parameter
    // Returns UserResponse, a DTO that transfers User data between the controller and service
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    // When request hits this controller, it gets validated from @Valid. Spring then checks fields
    // in RegisterRequest against added validation annotation (e.g., @Email, @Size, @NotBlank).
    // If not valid, returns 400 response and a MethodArgumentNotValidException
    // @RequestBody tells Spring to take the JSON data with an email, password, first & last name
    // and turn it into a RegisterRequest object
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // Check if the user exists in the database (called from activity service)
    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.existByUserId(userId));
    }
}
