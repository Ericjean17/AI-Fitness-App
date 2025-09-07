package com.fitness.gateway.user;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required") // email cannot be null, provides error msg when validation fails
    @Email(message = "Invalid email format") // must be an email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters") // min size
    private String password;

    private String keycloakId;

    private String firstName;
    private String lastName;
}
