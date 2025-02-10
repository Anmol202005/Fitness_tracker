package org.fitness.fitness.Service;

import java.util.ArrayList;
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
import org.fitness.fitness.Repository.WorkoutSessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class WorkoutPlanService {
    private final UserDataRepository userDataRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    public ResponseEntity<?> getWorkoutPlanForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        FitnessGoal userGoal = userDataRepository.getByUser(currentUser).getFitnessGoal();
        ActivityLevel userLevel = userDataRepository.getByUser(currentUser).getActivityLevel();
        List<WorkoutPlan> plan = workoutPlanRepository.findByGoal(userGoal);
        List<WorkOutPlanResponse> responses = new ArrayList<>();

        for (WorkoutPlan workoutPlan : plan) {
            WorkOutPlanResponse response = new WorkOutPlanResponse();
            response.setWorkoutId(workoutPlan.getId());
            response.setName(workoutPlan.getName());
            response.setTargetBodyPart(workoutPlan.getTargetBodyPart());
            response.setNumberOfExercises(String.valueOf(workoutPlan.getExercises().size()));
            response.setGoal(workoutPlan.getGoal());
            response.setDescription(workoutPlan.getDescription());
            if(workoutSessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlan.getId()).isPresent()){
                var session = workoutSessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlan.getId());
                response.setNumberOfExercisesCompleted(String.valueOf(session.get().getCompletedExercises()));

            }
            else {
                response.setNumberOfExercisesCompleted("0");
            }
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
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
        List<Exercise> finalExercises = new ArrayList<>();
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

    public ResponseEntity<?> getWorkoutPlans() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        List<WorkoutPlan> plan = workoutPlanRepository.findAll();
        List<WorkOutPlanResponse> responses = new ArrayList<>();
        for (WorkoutPlan workoutPlan : plan) {
            WorkOutPlanResponse response = new WorkOutPlanResponse();
            response.setWorkoutId(workoutPlan.getId());
            response.setName(workoutPlan.getName());
            response.setTargetBodyPart(workoutPlan.getTargetBodyPart());
            response.setNumberOfExercises(String.valueOf(workoutPlan.getExercises().size()));
            response.setGoal(workoutPlan.getGoal());
            response.setDescription(workoutPlan.getDescription());
            if(workoutSessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlan.getId()).isPresent()){
                var session = workoutSessionRepository.findByUserIdAndWorkoutPlanIdAndIsCompletedFalse(currentUser.getUserId(), workoutPlan.getId());
                response.setNumberOfExercisesCompleted(String.valueOf(session.get().getCompletedExercises()));
            }
            else {
                response.setNumberOfExercisesCompleted("0");
            }
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
    }

}

