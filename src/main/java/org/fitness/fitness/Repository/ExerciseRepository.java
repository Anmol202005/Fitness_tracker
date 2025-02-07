package org.fitness.fitness.Repository;

import java.util.List;

import org.fitness.fitness.Model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByTargetBody(String bodyPart);
}
