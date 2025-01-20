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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@Tag(name = "Workout", description = "APIs for managing workout activities such as adding, updating, or deleting workouts.")
public class WorkoutController {

    private final WorkoutService workoutService;

    /**
     * Add a new workout.
     * @param type Type of workout (e.g., running, cycling, etc.).
     * @param duration Duration of the workout in minutes.
     * @param date Date of the workout in yyyy-MM-dd format.
     * @return ResponseEntity containing the result of the operation.
     */
    @Operation(summary = "Add a new workout", description = "Adds a new workout entry for the current user with type, duration, and date.")
    @PostMapping
    public ResponseEntity<?> addWorkout(
            @Parameter(description = "Type of the workout (e.g., running, cycling, etc.)") @RequestParam String type,
            @Parameter(description = "Duration of the workout in minutes.") @RequestParam Double duration,
            @Parameter(description = "The date of the workout in yyyy-MM-dd format.") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return workoutService.addWorkout(type, duration, date);
    }

    /**
     * Get all workouts for the current user.
     * @return ResponseEntity containing a list of all workouts.
     */
    @Operation(summary = "Get all workouts", description = "Retrieves all workouts recorded for the current user.")
    @GetMapping
    public ResponseEntity<?> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    /**
     * Get workouts for the current user on a specific date.
     * @param date The date in yyyy-MM-dd format.
     * @return ResponseEntity containing a list of workouts for the given date.
     */
    @Operation(summary = "Get workouts by date", description = "Retrieves all workouts recorded for the current user on a specific date.")
    @GetMapping("/by-date")
    public ResponseEntity<?> getWorkoutsByDate(
            @Parameter(description = "The date in yyyy-MM-dd format to retrieve workouts.") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return workoutService.getWorkoutsByDate(date);
    }

    /**
     * Delete a workout by its ID.
     * @param id The ID of the workout to delete.
     * @return ResponseEntity containing the result of the operation.
     */
    @Operation(summary = "Delete workout by ID", description = "Deletes a workout entry by its ID for the current user.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutById(
            @Parameter(description = "The ID of the workout to delete.") @PathVariable long id) {
        return workoutService.deleteWorkoutById(id);
    }
}
