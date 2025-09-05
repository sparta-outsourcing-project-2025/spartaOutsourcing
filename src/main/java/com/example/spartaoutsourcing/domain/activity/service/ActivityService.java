package com.example.spartaoutsourcing.domain.activity.service;

import com.example.spartaoutsourcing.domain.activity.entity.Activity;
import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import com.example.spartaoutsourcing.domain.activity.repository.ActivityRepository;
import com.example.spartaoutsourcing.domain.task.entity.Task;
import com.example.spartaoutsourcing.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activitiesRepository;

    @Transactional
    public Activity save(ActivityType type, User user, Task task, String description){
        Activity activity = Activity.of(type, user, task, description);
        return activitiesRepository.save(activity);
    }
}
