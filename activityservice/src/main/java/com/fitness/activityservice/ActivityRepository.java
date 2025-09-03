package com.fitness.activityservice;

import com.fitness.activityservice.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Like JPA repository, MongoRepository lets ActivityRepository interface have access to all query methods
@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
    // The model stores an Activity so we need a list of them since userId can have multiple Activity
    List<Activity> findByUserId(String userId); // Hibernate will automatically generate this query
}
