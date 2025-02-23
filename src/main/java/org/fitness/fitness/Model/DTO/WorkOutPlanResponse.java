package org.fitness.fitness.Model.DTO;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fitness.fitness.Model.FitnessGoal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOutPlanResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long workoutId;
    private String name;
    private String description;
    private FitnessGoal goal;
    private String targetBodyPart;
    private String numberOfExercises;
    private String numberOfExercisesCompleted;
    private List<Long> incompleteExerciseIds;
}
