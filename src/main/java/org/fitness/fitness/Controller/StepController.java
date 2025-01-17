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

@RestController
@RequestMapping("/api/steps")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    /**
     * Add or update step count for the current user
     * @param stepCount Number of steps taken
     * @return ResponseEntity with success or failure message
     */
    @PostMapping
    public ResponseEntity<?> addOrUpdateStepCount(@RequestParam Integer stepCount) {
        return stepService.addOrUpdateStepCount(stepCount);
    }

    /**
     * Get step count for the current user on a specific date
     * @param date Date for which to retrieve the step count
     * @return ResponseEntity with step count or error message
     */
    @GetMapping("/by-date")
    public ResponseEntity<?> getStepCountByDate(@RequestParam LocalDate date) {
        return stepService.getStepCountByDate(date);
    }

    /**
     * Get all step counts for the current user
     * @return ResponseEntity with a list of step counts
     */
    @GetMapping
    public ResponseEntity<?> getAllStepCounts() {
        return stepService.getAllStepCounts();
    }
}
