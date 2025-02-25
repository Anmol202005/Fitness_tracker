package org.fitness.fitness.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.CalorieResponse;
import org.fitness.fitness.Model.DTO.FoodLogRequest;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.FoodLog;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Repository.FoodLogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class FoodLogService {
    final FoodLogRepository foodLogRepository;
    final org.fitness.fitness.service.GeminiService geminiService;

    public ResponseEntity<?> foodLog(FoodLogRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        String input = request.getName();
        String text = "Please provide the calories for the following food item:\n" +
            "Food: " + input + "\n" +
            "Portion: " + request.getDescription() + "\n" +
            "Rules:\n" +
            "1) Return only the numeric calorie value\n" +
            "2) Provide the total calories for the specified portion\n" +
            "4) Numbers only, no additional text";
        String caloriesResponse = geminiService.getGeneratedText(text);
        //        if(!calories.matches("[0-9]+")){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
//                        .builder()
//                        .message("Invalid inputs")
//                        .build());
//        }
        FoodLog foodLog = new FoodLog();
        foodLog.setUser(currentUser);
        foodLog.setFoodName(request.getName());
        String calories = caloriesResponse.replace("\n", "").trim();
        foodLog.setCalories(calories);
        foodLog.setDescription(request.getDescription());


        return ResponseEntity.ok(foodLog);
    }

    public ResponseEntity<?> totalCaloriesForDay(int i) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var currentUser = (User) authentication.getPrincipal();
    LocalDate today = LocalDate.now();
    LocalDate startDate;

    if (i == 0) { // Calories for today
        startDate = today;
    } else if (i == 1) { // Calories for the past week
        startDate = today.minusDays(7);
    } else if (i == 2) { // Calories for the past month
        startDate = today.minusDays(30);
    } else {
        return ResponseEntity.badRequest().body("Invalid input! Use 0 for today, 1 for this week, or 2 for this month.");
    }

    List<FoodLog> foodLogs = foodLogRepository.findByUserAndDateBetween(currentUser, startDate, today);
    double totalCalories = 0;

    for (FoodLog foodLog : foodLogs) {
        try {
            totalCalories += Double.parseDouble(foodLog.getCalories());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid calorie format in food log.");
        }
    }
     return ResponseEntity.ok().body(CalorieResponse
                        .builder()
                        .calorieConsumed(totalCalories)
                        .build());
}

    public ResponseEntity<?> deleteFoodLog(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        if (foodLogRepository.existsById(id)) {
            FoodLog foodLog = foodLogRepository.findById(id).get();
            if(!foodLog.getUser().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                        .builder()
                        .message("Access denied")
                        .build());
            }
            foodLogRepository.deleteById(id);
            return ResponseEntity.ok("Food log deleted successfully");
        } else {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                        .builder()
                        .message("Not found")
                        .build());        }
    }

    public ResponseEntity<?> getFoodLogsByDate(LocalDate date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        List<FoodLog> foodLogs = foodLogRepository.findByUserAndDate(currentUser, date);
        if (foodLogs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No food logs found for the given date");
        }
        return ResponseEntity.ok(foodLogs);
    }

    public ResponseEntity<?> getFoodLogById(Long id) {
        Optional<FoodLog> foodLog = foodLogRepository.findById(id);
        if (foodLog.isPresent()) {
            return ResponseEntity.ok(foodLog.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food log not found");
        }
    }

    public ResponseEntity<?> getAllFoodLogs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        List<FoodLog> foodLogs = foodLogRepository.findByUser(currentUser);
        return ResponseEntity.ok(foodLogs);
    }
}
