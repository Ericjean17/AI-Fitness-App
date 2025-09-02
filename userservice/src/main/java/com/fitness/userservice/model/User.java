package com.fitness.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity // Instances of this class can be mapped to rows in database table
@Table(name = "users") // Links this entity to users table
@Data // Generates getters and setters, toString(), required constructor
public class User {
    @Id // Indicates id will be the primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @CreationTimestamp // Time generated once when entity is inserted into database
    private LocalDateTime createdAt;

    @UpdateTimestamp // Time regenerated every time entity is updated in database by Hibernate
    private LocalDateTime updatedAt;
}
