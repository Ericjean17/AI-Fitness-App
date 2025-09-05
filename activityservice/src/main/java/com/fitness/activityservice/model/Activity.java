package com.fitness.activityservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "activities") // Entity for MongoDB collections (similar to @Entity)
@Data
@Builder // Lombok automates implementation of Builder design pattern
@AllArgsConstructor // Generates a constructor that initializes all fields
@NoArgsConstructor // Jackson needs it to create an empty object
                    // before populating its fields during deserialization (JSON -> Object)
public class Activity {
    @Id
    private String id;
    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;

    @Field("metrics") // Maps "additionalMetrics" to "metrics" in NoSQL database
    private Map<String, Object> additionalMetrics;

    // Not using timestamp since those are JPA/relational db annotations
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
