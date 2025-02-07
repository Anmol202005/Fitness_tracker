package org.fitness.fitness.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.ActivityLevel;
import org.fitness.fitness.Model.DTO.ExercisesDto;
import org.fitness.fitness.Model.DTO.WorkOutPlanResponse;
import org.fitness.fitness.Model.Exercise;
import org.fitness.fitness.Model.FitnessGoal;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Model.WorkoutPlan;
import org.fitness.fitness.Repository.UserDataRepository;
import org.fitness.fitness.Repository.WorkoutPlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class WorkoutPlanService {
    private final UserDataRepository userDataRepository;
    private final WorkoutPlanRepository workoutPlanRepository;

    public ResponseEntity<?> getWorkoutPlanForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        FitnessGoal userGoal = userDataRepository.getByUser(currentUser).getFitnessGoal();
        ActivityLevel userLevel = userDataRepository.getByUser(currentUser).getActivityLevel();
        List<WorkoutPlan> plan = workoutPlanRepository.findByGoal(userGoal);
        return ResponseEntity.ok().body(WorkOutPlanResponse
                    .builder()
                    .plans(plan)
                    .build());
    }

    public ResponseEntity<?> getExercisesForWorkoutPlan(Long workoutPlanId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        WorkoutPlan workoutPlan = workoutPlanRepository.getById(workoutPlanId);
        ActivityLevel userLevel = userDataRepository.getByUser(currentUser).getActivityLevel();
        int point =0;
        if(userLevel == ActivityLevel.BEGINNER){
            point = 10;
        }
        if( userLevel == ActivityLevel.ADVANCED){
            point = -5;
        }
        List<Exercise> exercises = workoutPlan.getExercises();
        List<Exercise> finalExercises = List.of();
        for (Exercise exercise : exercises) {
            if(exercise.getReps()!=null){
                exercise.setReps(exercise.getReps()-point);
            }
            else{
                exercise.setDuration(exercise.getDuration()-point);
            }
            finalExercises.add(exercise);
        }
        return ResponseEntity.ok().body(ExercisesDto
                    .builder()
                    .exercises(finalExercises)
                    .build());
    }
}

