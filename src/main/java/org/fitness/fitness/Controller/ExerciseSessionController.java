package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Service.ExerciseSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercise-session")
@RequiredArgsConstructor
public class ExerciseSessionController {

    private final ExerciseSessionService exerciseSessionService;

    /**
     * Start a new exercise session.
     */
    @PostMapping("/start/{exerciseId}")
    public ResponseEntity<?> startExerciseSession(@PathVariable Long exerciseId) {
        return exerciseSessionService.startExerciseSession(exerciseId);
    }

    /**
     * Mark an exercise session as completed.
     */
    @PostMapping("/complete/{sessionId}")
    public ResponseEntity<?> completeExerciseSession(@PathVariable Long sessionId) {
        return exerciseSessionService.completeExerciseSession(sessionId);
    }

    /**
     * Get total calories burned for today (0), week (1), or month (2).
     */
    @GetMapping("/calories/{type}")
    public ResponseEntity<?> getTotalCaloriesBurned(@PathVariable int type) {
        return exerciseSessionService.getTotalCaloriesBurned(type);
    }
}
