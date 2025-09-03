package com.fitness.activityservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration // This class contains bean definitions or Spring-related settings.
                // Ensures Spring loads MongoConfig in config class, and applies EnableMongoAuditing
@EnableMongoAuditing // Automatically fills in @CreatedDate, @LastModifiedDate, etc
public class MongoConfig {
}
