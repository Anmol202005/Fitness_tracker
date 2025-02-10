package org.fitness.fitness.Service;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.ActivityLevel;
import org.fitness.fitness.Model.DTO.CalorieBurnedResponse;
import org.fitness.fitness.Model.Exercise;
import org.fitness.fitness.Model.ExerciseSession;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Repository.ExerciseRepository;
import org.fitness.fitness.Repository.ExerciseSessionRepository;
import org.fitness.fitness.Repository.UserDataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseSessionService {
    private final ExerciseSessionRepository exerciseSessionRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserDataRepository userDataRepository;

    /**
     * Start an exercise session for the current user.
     */
    public ResponseEntity<?> startExerciseSession(Long exerciseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);
        Exercise exercise = exerciseOpt.get();
        if (exerciseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Exercise not found.");
        }
         ActivityLevel userLevel = userDataRepository.getByUser(currentUser).getActivityLevel();
        int point =0;
        if(userLevel == ActivityLevel.BEGINNER){
            point = 10;
        }
        if( userLevel == ActivityLevel.ADVANCED){
            point = -5;
        }
        if(exercise.getReps()!=null){
            exercise.setReps(exercise.getReps()-point);
        }
        else {
            exercise.setDuration(exercise.getDuration()-point);
        }

        ExerciseSession session = new ExerciseSession();
        session.setUserId(currentUser.getUserId());
        session.setExercise(exercise);
        session.setCompleted(false);

        return ResponseEntity.ok(exerciseSessionRepository.save(session));
    }

    /**
     * Mark an exercise session as completed.
     */
    public ResponseEntity<?> completeExerciseSession(Long sessionId) {
        Optional<ExerciseSession> sessionOpt = exerciseSessionRepository.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Session not found.");
        }

        ExerciseSession session = sessionOpt.get();
        session.setCompleted(true);
        session.setDate(LocalDate.now());

        exerciseSessionRepository.save(session);
        return ResponseEntity.ok("Exercise session marked as completed.");
    }


   public ResponseEntity<?> getTotalCaloriesBurned(int i) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var currentUser = (User) authentication.getPrincipal();
    Integer calories = 0;
    LocalDate today = LocalDate.now();

    List<ExerciseSession> exercises ;

    if (i == 0) { // Calories burned today
        exercises = exerciseSessionRepository.findByUserIdAndIsCompletedTrueAndDate(currentUser.getUserId(), today);
    } else if (i == 1) { // Calories burned in the past week
        LocalDate weekStart = today.minusDays(7);
        exercises = exerciseSessionRepository.findByUserIdAndIsCompletedTrueAndDateBetween(currentUser.getUserId(), weekStart, today);
    } else if (i == 2) { // Calories burned in the past month
        LocalDate monthStart = today.minusDays(30);
        exercises = exerciseSessionRepository.findByUserIdAndIsCompletedTrueAndDateBetween(currentUser.getUserId(), monthStart, today);
    } else {
        return ResponseEntity.badRequest().body("Invalid input! Use 0 for today, 1 for this week, or 2 for this month.");
    }

    for (ExerciseSession exercise : exercises) {
        calories += exercise.getExercise().getCalories();
    }

    return ResponseEntity.ok().body(CalorieBurnedResponse
            .builder()
            .calorieBurned(calories)
            .build());
}

}
