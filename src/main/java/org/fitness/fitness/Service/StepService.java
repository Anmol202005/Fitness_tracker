package org.fitness.fitness.Service;


import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.Step;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Repository.StepRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;

    /**
     * Add or update step count for the current user and date
     * @param stepCount Number of steps taken
     * @return ResponseEntity with success or failure message
     */
    public ResponseEntity<?> addOrUpdateStepCount(Integer stepCount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        LocalDate today = LocalDate.now();

        Step step = stepRepository.findByUserAndDate(currentUser, today)
                .orElse(new Step());
        step.setUser(currentUser);
        step.setDate(today);
        step.setStepCount(stepCount);

        stepRepository.save(step);

        return ResponseEntity.ok().body(ResponseMessage
                .builder()
                .message("Step count successfully added or updated")
                .build());
    }

    /**
     * Get step count for the current user and date
     * @param date Date to fetch the step count for
     * @return ResponseEntity with step count or error message
     */
    public ResponseEntity<?> getStepCountByDate(LocalDate date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        Step step = stepRepository.findByUserAndDate(currentUser, date)
                .orElse(null);

        if (step == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessage
                    .builder()
                    .message("No step count data found for the given date")
                    .build());
        }

        return ResponseEntity.ok().body(step);
    }

    /**
     * Get all step counts for the current user
     * @return ResponseEntity with a list of step counts
     */
    public ResponseEntity<?> getAllStepCounts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        List<Step> steps = stepRepository.findByUser(currentUser);

        return ResponseEntity.ok().body(steps);
    }
}

