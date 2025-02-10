package org.fitness.fitness.Repository;

import org.fitness.fitness.Model.WaterIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {
    List<WaterIntake> findByUserIdAndDate(Long userId, LocalDate date);

    List<WaterIntake> findByUserIdAndDateBetween(Long userId, LocalDate weekStart, LocalDate today);
}

