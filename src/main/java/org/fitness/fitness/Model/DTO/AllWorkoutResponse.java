package org.fitness.fitness.Model.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.Workout;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class AllWorkoutResponse {
    private List<Workout> workouts;
    private Double totalCalories;
}