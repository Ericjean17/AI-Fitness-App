package com.fitness.gateway.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse { // Will have same fields as User model from database
    private String id;
    private String keycloakId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
