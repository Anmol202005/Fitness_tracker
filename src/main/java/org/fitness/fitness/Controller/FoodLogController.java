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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/foodlogs")
@RequiredArgsConstructor
@Tag(name = "Food Logs", description = "APIs for managing food logs and calorie tracking.")
public class FoodLogController {

    private final FoodLogService foodLogService;

    /**
     * Add a new food log for the current user.
     */
    @Operation(summary = "Add a Food Log", description = "Adds a new food log for the current authenticated user.")
    @PostMapping
    public ResponseEntity<?> addFoodLog(
            @Parameter(description = "Details of the food log to be added.") @RequestBody FoodLogRequest foodLogRequest) {
        return foodLogService.foodLog(foodLogRequest);
    }

    /**
     * Get all food logs for the current user.
     */
    @Operation(summary = "Get All Food Logs", description = "Retrieves all food logs for the current authenticated user.")
    @GetMapping
    public ResponseEntity<?> getAllFoodLogs() {
        return foodLogService.getAllFoodLogs();
    }

    /**
     * Get all food logs for a specific date.
     */
    @Operation(summary = "Get Food Logs by Date", description = "Retrieves all food logs for the current authenticated user on a specific date.")
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getFoodLogsByDate(
            @Parameter(description = "The date to filter the food logs by.") @PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return foodLogService.getFoodLogsByDate(localDate);
    }

    /**
     * Get a specific food log by ID.
     */
    @Operation(summary = "Get a Food Log by ID", description = "Retrieves a specific food log by its ID for the current authenticated user.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodLogById(
            @Parameter(description = "The ID of the food log.") @PathVariable("id") Long id) {
        return foodLogService.getFoodLogById(id);
    }

    /**
     * Delete a specific food log by ID.
     */
    @Operation(summary = "Delete a Food Log", description = "Deletes a specific food log by its ID for the current authenticated user.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFoodLog(
            @Parameter(description = "The ID of the food log to be deleted.") @PathVariable("id") Long id) {
        return foodLogService.deleteFoodLog(id);
    }

    /**
     * Get the total calories consumed for a specific day.
     */
    @Operation(summary = "Get Total Calories for the Day", description = "Retrieves the total calories consumed by the current authenticated user on a specific date.")
    @GetMapping("/totalCalories/date/{date}")
    public ResponseEntity<?> getTotalCaloriesForDay(
            @Parameter(description = "The date to calculate the total calories for.") @PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        double totalCalories = foodLogService.totalCaloriesForDay(localDate);
        return ResponseEntity.ok().body(ResponseMessage
                .builder()
                .message("Total calories consumed for the day: " + totalCalories)
                .build());
    }
}
