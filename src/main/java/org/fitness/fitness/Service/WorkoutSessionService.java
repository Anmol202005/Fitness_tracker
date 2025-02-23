package org.fitness.fitness.Service;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.Exercise;
import org.fitness.fitness.Model.ExerciseSession;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Model.WorkoutSession;
import org.fitness.fitness.Repository.ExerciseRepository;
import org.fitness.fitness.Repository.ExerciseSessionRepository;
import org.fitness.fitness.Repository.UserDataRepository;
import org.fitness.fitness.Repository.WorkoutPlanRepository;
import org.fitness.fitness.Repository.WorkoutSessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository sessionRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseSessionRepository exerciseSessionRepository;
    private final UserDataRepository userDataRepository;
    private final WorkoutPlanRepository workoutPlanRepository;

    public void startWorkout(Long workoutPlanId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        var workoutPlan = workoutPlanRepository.findById(workoutPlanId).get();

        WorkoutSession session = WorkoutSession.builder()
                .userId(currentUser.getUserId())
                .workoutPlanId(workoutPlanId)
                .startTime(LocalDateTime.now())
                .completedExercises(0)
                .incompleteExerciseId(workoutPlan.getExercises().stream()
                                                 .map(Exercise::getId)
                                                 .collect(Collectors.toList()))
                .isCompleted(false)
                .build();

        sessionRepository.save(session);

    }

    public ResponseEntity<?> completeExercise(Long workoutPlanId, Long exerciseId) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        if( workoutPlanId != null) {
            if (!sessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlanId).isPresent()) {
                startWorkout(workoutPlanId);
            }

            var session = sessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlanId);

            WorkoutSession activeSession = session.get();
            activeSession.setCompletedExercises(activeSession.getCompletedExercises() + 1);
            List<Long> inComp = activeSession.getIncompleteExerciseId();
            if(inComp.contains(exerciseId)) {
                inComp.remove(exerciseId);
                activeSession.setIncompleteExerciseId(inComp);
            }
            else{
                return ResponseEntity.badRequest().body(ResponseMessage.builder()
                                        .message("Invalid ExerciseId").build());
            }
            sessionRepository.save(activeSession);

            Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);
        Exercise exercise = exerciseOpt.get();
        if (exerciseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Exercise not found.");
        }

        ExerciseSession eXsession = new ExerciseSession();
        eXsession.setUserId(currentUser.getUserId());
        eXsession.setExercise(exercise);
        eXsession.setCompleted(true);
        exerciseSessionRepository.save(eXsession);
        if(inComp.isEmpty()){
            activeSession.setCompleted(true);
            sessionRepository.save(activeSession);
            var Data = userDataRepository.findById(currentUser.getUserId()).get();
            Data.setCoin(Data.getCoin() + 20);
            return ResponseEntity.ok().body(ResponseMessage.builder().message("Workout finished!").build());

        }
        return ResponseEntity.ok().body(exerciseRepository.findById(inComp.get(0)));


        }
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);
        Exercise exercise = exerciseOpt.get();
        if (exerciseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Exercise not found.");
        }

        ExerciseSession session = new ExerciseSession();
        session.setUserId(currentUser.getUserId());
        session.setExercise(exercise);
        session.setCompleted(true);
        exerciseSessionRepository.save(session);

        return ResponseEntity.ok().body(ResponseMessage.builder().message("EXERCISE COMPLETED").build());

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
