package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Service.WorkoutPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workout")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @GetMapping("/plan")
    public ResponseEntity<?> getWorkoutPlanForUser() {
        return workoutPlanService.getWorkoutPlanForUser();
    }

    @GetMapping("/plan/{id}/exercises")
    public ResponseEntity<?> getExercisesForWorkoutPlan(@PathVariable("id") Long workoutPlanId) {
        return workoutPlanService.getExercisesForWorkoutPlan(workoutPlanId);
    }
}