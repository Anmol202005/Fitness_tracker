package org.fitness.fitness.Repository;

import java.time.LocalDate;
import java.util.List;

import org.fitness.fitness.Model.FoodLog;
import org.fitness.fitness.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodLogRepository extends JpaRepository<FoodLog, Long> {

    List<FoodLog> findByUserAndDate(User currentUser, LocalDate date);

    List<FoodLog> findByUser(User currentUser);

    List<FoodLog> findByUserAndDateBetween(User currentUser, LocalDate startDate, LocalDate today);
}
