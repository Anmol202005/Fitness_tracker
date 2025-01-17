package org.fitness.fitness.Service;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.AllWorkoutResponse;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.DTO.WorkoutResponse;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Model.Workout;
import org.fitness.fitness.Repository.WorkoutRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    /**
     * Add a new workout entry for the current user.
     */
    public ResponseEntity<?> addWorkout(String type, Double duration, LocalDate date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        if (type == null || duration == null || duration <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("Invalid type or duration")
                    .build());
        }

        double caloriesPerMinute;
        switch (type.toLowerCase(Locale.ROOT)) {
            case "running":
                caloriesPerMinute = 10;
                break;
            case "cycling":
                caloriesPerMinute = 8;
                break;
            case "swimming":
                caloriesPerMinute = 12;
                break;
            case "weightlifting":
                caloriesPerMinute = 6;
                break;
            case "yoga":
                caloriesPerMinute = 4;
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                        .builder()
                        .message("Unsupported workout type")
                        .build());
        }

        double totalCalories = duration * caloriesPerMinute;

        Workout workout = new Workout();
        workout.setUser(currentUser);
        workout.setType(type);
        workout.setDuration(duration);
        workout.setDate(date);
        workout.setCalories(totalCalories);

        workoutRepository.save(workout);

        return ResponseEntity.ok().body(WorkoutResponse
                .builder()
                .message("Workout successfully added")
                .totalCalories(totalCaloriesBurnedInDay(date))
                .build());
    }

    /**
     * Get all workouts for the current user.
     */
    public ResponseEntity<?> getAllWorkouts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        List<Workout> workouts = workoutRepository.findByUser(currentUser);
        return ResponseEntity.ok().body(workouts);
    }

    public ResponseEntity<?> deleteWorkoutById(long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        if(!workoutRepository.existsById(id)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                        .builder()
                        .message("Invalid workout id")
                        .build());
        }
        if(!workoutRepository.findById(id).get().getUser().equals(currentUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                        .builder()
                        .message("User don't have access to this workout")
                        .build());
        }
        LocalDate date = workoutRepository.findById(id).get().getDate();
        workoutRepository.deleteById(id);
        return ResponseEntity.ok().body(WorkoutResponse
                .builder()
                .message("Workout successfully deleted")
                .totalCalories(totalCaloriesBurnedInDay(date))
                .build());
    }

    public ResponseEntity<?> getWorkoutsByDate(LocalDate date) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var currentUser = (User) authentication.getPrincipal();

    List<Workout> workouts = workoutRepository.findByUserAndDate(currentUser, date);
    if (workouts.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessage
                .builder()
                .message("No workouts found for the given date")
                .build());
    }
    return ResponseEntity.ok().body(AllWorkoutResponse
                .builder()
                .workouts(workouts)
                .totalCalories(totalCaloriesBurnedInDay(date))
                .build());
    }

    private Double totalCaloriesBurnedInDay(LocalDate date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        List<Workout> workouts = workoutRepository.findByUserAndDate(currentUser, date);
        Double totalCalories = 0.0;
        for(Workout workout : workouts) {
            totalCalories += workout.getCalories();
        }
        return totalCalories;
    }
}

