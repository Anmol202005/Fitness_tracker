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

    /**
     * Get the workout plan for the logged-in user based on their fitness goal.
     */
    @GetMapping("/user-plan")
    public ResponseEntity<?> getWorkoutPlanForUser() {
        return workoutPlanService.getWorkoutPlanForUser();
    }

    /**
     * Get all available workout plans along with progress data.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllWorkoutPlans() {
        return workoutPlanService.getWorkoutPlans();
    }

    /**
     * Get exercises for a specific workout plan.
     * @param workoutPlanId - ID of the workout plan
     */
    @GetMapping("/{workoutPlanId}/exercises")
    public ResponseEntity<?> getExercisesForWorkoutPlan(@PathVariable Long workoutPlanId) {
        return workoutPlanService.getExercisesForWorkoutPlan(workoutPlanId);
    }
}
