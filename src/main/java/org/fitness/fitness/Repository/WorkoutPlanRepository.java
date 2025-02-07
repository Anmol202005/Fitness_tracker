package org.fitness.fitness.Repository;

import java.util.List;

import org.fitness.fitness.Model.FitnessGoal;
import org.fitness.fitness.Model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    List<WorkoutPlan> findByGoal(FitnessGoal userGoal);
}
