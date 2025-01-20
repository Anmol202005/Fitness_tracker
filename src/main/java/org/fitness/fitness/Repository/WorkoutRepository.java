package org.fitness.fitness.Repository;

import java.time.LocalDate;
import java.util.List;

import org.fitness.fitness.Model.User;
import org.fitness.fitness.Model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUser(User currentUser);

    List<Workout> findByUserAndDate(User currentUser, LocalDate date);
}
