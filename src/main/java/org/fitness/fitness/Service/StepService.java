package org.fitness.fitness.Service;


import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.DTO.StepResponse;
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
import java.util.Optional;

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

    public ResponseEntity<?> getStepCountForUser(int i){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        int step = 0;
        LocalDate today = LocalDate.now();

        if (i == 0) {
        Optional<Step> steps = stepRepository.findByUserAndDate(currentUser, today);
        int stepCount = steps.map(Step::getStepCount).orElse(0);
         return ResponseEntity.ok().body(StepResponse
                    .builder()
                    .stepCount(step)
                    .build());

    } else if (i == 1) {
        LocalDate weekStart = today.minusDays(6);
        Optional<Step> steps = stepRepository.findByUserAndDateBetween(currentUser, weekStart, today);
        int stepCount = steps.map(Step::getStepCount).orElse(0);
        return ResponseEntity.ok().body(StepResponse
                    .builder()
                    .stepCount(step)
                    .build());

    } else if (i == 2) {
        LocalDate monthStart = today.withDayOfMonth(1);
        Optional<Step> steps = stepRepository.findByUserAndDateBetween(currentUser, monthStart, today);
        int stepCount = steps.map(Step::getStepCount).orElse(0);
         return ResponseEntity.ok().body(StepResponse
                    .builder()
                    .stepCount(step)
                    .build());

    }
    else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("Invalid Input nigga")
                    .build());
    }
    }
}


