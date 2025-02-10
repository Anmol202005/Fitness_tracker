package org.fitness.fitness.Repository;

import org.fitness.fitness.Model.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    Optional<WorkoutSession> findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(Long userId, Long workoutPlanId);
}
