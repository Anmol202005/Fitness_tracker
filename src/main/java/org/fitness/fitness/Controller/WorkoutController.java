package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Service.WorkoutService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    /**
     * Add a new workout.
     * @param type Type of workout (e.g., running, cycling, etc.).
     * @param duration Duration of the workout in minutes.
     * @param date Date of the workout in yyyy-MM-dd format.
     * @return ResponseEntity containing the result of the operation.
     */
    @PostMapping
    public ResponseEntity<?> addWorkout(
            @RequestParam String type,
            @RequestParam Double duration,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return workoutService.addWorkout(type, duration, date);
    }

    /**
     * Get all workouts for the current user.
     * @return ResponseEntity containing a list of all workouts.
     */
    @GetMapping
    public ResponseEntity<?> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    /**
     * Get workouts for the current user on a specific date.
     * @param date The date in yyyy-MM-dd format.
     * @return ResponseEntity containing a list of workouts for the given date.
     */
    @GetMapping("/by-date")
    public ResponseEntity<?> getWorkoutsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return workoutService.getWorkoutsByDate(date);
    }

    /**
     * Delete a workout by its ID.
     * @param id The ID of the workout to delete.
     * @return ResponseEntity containing the result of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutById(@PathVariable long id) {
        return workoutService.deleteWorkoutById(id);
    }
}
