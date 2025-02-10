package org.fitness.fitness.Model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
public class Exercise {
    @Id
    private Long id;
    private String name;
    private String gifUrl;
    private Integer reps;
    private Integer duration;
    private Integer restTime;
    private String targetBody;
    private Integer calories;

    @ManyToMany(mappedBy = "exercises")
    private List<WorkoutPlan> workoutPlans;
}
