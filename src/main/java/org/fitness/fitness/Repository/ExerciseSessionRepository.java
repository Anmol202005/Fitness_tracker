package org.fitness.fitness.Repository;


import org.fitness.fitness.Model.ExerciseSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseSessionRepository extends JpaRepository<ExerciseSession, Long> {
    List<ExerciseSession> findByUserIdAndIsCompletedFalse(Long userId);

    List<ExerciseSession> findByUserIdAndIsCompletedTrue(Long userId);

    List<ExerciseSession> findByUserIdAndIsCompletedTrueAndDate(Long userId, LocalDate today);

    List<ExerciseSession> findByUserIdAndIsCompletedTrueAndDateBetween(Long userId, LocalDate weekStart, LocalDate today);
}
