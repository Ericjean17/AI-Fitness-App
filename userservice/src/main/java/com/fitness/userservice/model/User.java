package com.fitness.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity // Hibernate will map instances of this class to database table
@Table(name = "users") // Entity links to users table
@Data // Generates getters and setters, toString(), equals(), hashCode(), constructor with lombok
public class User {
    @Id // Indicates id will be the primary key of entity
    @GeneratedValue(strategy = GenerationType.UUID) // Hibernate generates primary key as UUID
    private String id;

    @Column(unique = true, nullable = false) // Unique email and cannot be null
    private String email;

    @Column(nullable = false) // these fields cannot have a null value in database
    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING) // Tells JPA how to store enum in database
    private UserRole role = UserRole.USER;

    @CreationTimestamp // Hibernate sets timestamp when entity is first created
    private LocalDateTime createdAt;

    @UpdateTimestamp // Updates timestamp whenever entity is updated
    private LocalDateTime updatedAt;
}
