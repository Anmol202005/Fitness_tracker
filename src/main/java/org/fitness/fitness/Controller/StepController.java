package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Service.StepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/steps")
@RequiredArgsConstructor
@Tag(name = "Step Tracking", description = "APIs for managing step counts and tracking daily steps for users.")
public class StepController {

    private final StepService stepService;

    /**
     * Add or update step count for the current user
     * @param stepCount Number of steps taken
     * @return ResponseEntity with success or failure message
     */
    @Operation(summary = "Add or Update Step Count", description = "Adds or updates the step count for the current authenticated user.")
    @PostMapping("/add")
    public ResponseEntity<?> addOrUpdateStepCount(
            @Parameter(description = "Number of steps taken by the user.") @RequestParam Integer stepCount) {
        return stepService.addOrUpdateStepCount(stepCount);
    }

    /**
     * Get step count for the current user on a specific date
     * @param date Date for which to retrieve the step count
     * @return ResponseEntity with step count or error message
     */
    @Operation(summary = "Get Step Count by Date", description = "Retrieves the step count for the current authenticated user on a specific date.")
    @GetMapping("/by-date")
    public ResponseEntity<?> getStepCountByDate(
            @Parameter(description = "The date for which the step count is requested.") @RequestParam LocalDate date) {
        return stepService.getStepCountByDate(date);
    }

    /**
     * Get all step counts for the current user
     * @return ResponseEntity with a list of step counts
     */
    @Operation(summary = "Get All Step Counts", description = "Retrieves all step counts for the current authenticated user.")
    @GetMapping("get-all")
    public ResponseEntity<?> getAllStepCounts() {
        return stepService.getAllStepCounts();
    }
}
