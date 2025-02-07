package org.fitness.fitness.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.ActivityLevel;
import org.fitness.fitness.Model.DTO.ExercisesDto;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.Exercise;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Repository.ExerciseRepository;
import org.fitness.fitness.Repository.UserDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserDataRepository userDataRepository;

    public ResponseEntity<?> getExerciseById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        var exercise = exerciseRepository.findById(id).orElse(null);
        if (exercise == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessage
                    .builder()
                    .message("Invalid exercise ID")
                    .build());
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
        return ResponseEntity.ok(exercise);
    }
    public ResponseEntity<?> getExerciseByBodyPart(String bodyPart) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        List<Exercise> exercises = exerciseRepository.findByTargetBody(bodyPart);
        ActivityLevel userLevel = userDataRepository.getByUser(currentUser).getActivityLevel();
        int point =0;
        if(userLevel == ActivityLevel.BEGINNER){
            point = 10;
        }
        if( userLevel == ActivityLevel.ADVANCED){
            point = -5;
        }
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
