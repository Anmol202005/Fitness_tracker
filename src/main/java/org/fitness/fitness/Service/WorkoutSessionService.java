package org.fitness.fitness.Service;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Model.WorkoutSession;
import org.fitness.fitness.Repository.WorkoutSessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository sessionRepository;

    public ResponseEntity<?> startWorkout(Long workoutPlanId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        // Check if an active session already exists for this workout
        var existingSession = sessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlanId);
        if (existingSession.isPresent()) {
            return ResponseEntity.ok().body(ResponseMessage.builder().message("Workout already in progress").build());
        }

        WorkoutSession session = WorkoutSession.builder()
                .userId(currentUser.getUserId())
                .workoutPlanId(workoutPlanId)
                .startTime(LocalDateTime.now())
                .completedExercises(0)
                .isCompleted(false)
                .build();

        sessionRepository.save(session);
        return ResponseEntity.ok().body(ResponseMessage.builder().message("Workout started!").build());
    }

    public ResponseEntity<?> completeExercise(Long workoutPlanId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        var session = sessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlanId);
        if (session.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseMessage.builder().message("No active workout session found!").build());
        }

        WorkoutSession activeSession = session.get();
        activeSession.setCompletedExercises(activeSession.getCompletedExercises() + 1);
        sessionRepository.save(activeSession);

        return ResponseEntity.ok().body(ResponseMessage.builder()
                .message("Exercise completed! Total: " + activeSession.getCompletedExercises())
                .build());
    }

    public ResponseEntity<?> finishWorkout(Long workoutPlanId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        var session = sessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlanId);
        if (session.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseMessage.builder().message("No active workout session found!").build());
        }

        WorkoutSession activeSession = session.get();
        activeSession.setCompleted(true);
        sessionRepository.save(activeSession);

        return ResponseEntity.ok().body(ResponseMessage.builder().message("Workout finished!").build());
    }
}
