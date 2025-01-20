package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.FoodLogRequest;
import org.fitness.fitness.Service.FoodLogService;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/foodlogs")
@RequiredArgsConstructor
public class FoodLogController {

    private final FoodLogService foodLogService;

    /**
     * Add a new food log for the current user.
     */
    @PostMapping
    public ResponseEntity<?> addFoodLog(@RequestBody FoodLogRequest foodLogRequest) {
        return foodLogService.foodLog(foodLogRequest);
    }

    /**
     * Get all food logs for the current user.
     */
    @GetMapping
    public ResponseEntity<?> getAllFoodLogs() {
        return foodLogService.getAllFoodLogs();
    }

    /**
     * Get all food logs for a specific date.
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getFoodLogsByDate(@PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return foodLogService.getFoodLogsByDate(localDate);
    }

    /**
     * Get a specific food log by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodLogById(@PathVariable("id") Long id) {
        return foodLogService.getFoodLogById(id);
    }

    /**
     * Delete a specific food log by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFoodLog(@PathVariable("id") Long id) {
        return foodLogService.deleteFoodLog(id);
    }

    /**
     * Get the total calories consumed for a specific day.
     */
    @GetMapping("/totalCalories/date/{date}")
    public ResponseEntity<?> getTotalCaloriesForDay(@PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        double totalCalories = foodLogService.totalCaloriesForDay(localDate);
        return ResponseEntity.ok().body(ResponseMessage
                .builder()
                .message("Total calories consumed for the day"+totalCalories)
                .build());
    }
}

