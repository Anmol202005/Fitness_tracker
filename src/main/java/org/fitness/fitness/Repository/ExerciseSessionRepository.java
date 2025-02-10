package org.fitness.fitness.Repository;

import org.fitness.fitness.Model.Exercise;
import org.fitness.fitness.Model.ExerciseSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseSessionRepository extends JpaRepository<ExerciseSession, Long> {
    List<ExerciseSession> findByUserIdAndIsCompletedFalse(Long userId);

    List<Exercise> findByUserIdAndIsCompletedTrue(Long userId);

    List<Exercise> findByUserIdAndIsCompletedTrueAndDate(Long userId, LocalDate today);

    List<Exercise> findByUserIdAndIsCompletedTrueAndDateBetween(Long userId, LocalDate weekStart, LocalDate today);
}
