package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Service.WorkoutSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workout-session")
@RequiredArgsConstructor
public class WorkoutSessionController {

    private final WorkoutSessionService sessionService;

    @PostMapping("/start")
    public ResponseEntity<?> startWorkout(@RequestParam Long workoutPlanId) {
        return sessionService.startWorkout(workoutPlanId);
    }

    @PostMapping("/complete-exercise")
    public ResponseEntity<?> completeExercise(@RequestParam Long workoutPlanId, Long exerciseId) {
        return sessionService.completeExercise(workoutPlanId, exerciseId);
    }

    @PostMapping("/finish")
    public ResponseEntity<?> finishWorkout(@RequestParam Long workoutPlanId) {
        return sessionService.finishWorkout(workoutPlanId);
    }
}
