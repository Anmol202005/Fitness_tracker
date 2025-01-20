package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.UserDataDTO;
import org.fitness.fitness.Service.UserDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user-data")
@RequiredArgsConstructor
@Tag(name = "User Data", description = "APIs for managing user data such as personal details, health info, etc.")
public class UserDataController {

    private final UserDataService userDataService;

    /**
     * Add user data
     * @param userDataDTO Data to be added
     * @return ResponseEntity with success or failure message
     */
    @Operation(summary = "Add User Data", description = "Adds new user data like personal information, health stats, etc.")
    @PostMapping
    public ResponseEntity<?> addUserData(
            @Parameter(description = "User data to be added, including personal and health-related details.") @RequestBody UserDataDTO userDataDTO) {
        return userDataService.addUserData(userDataDTO);
    }

    /**
     * Update user data
     * @param userDataDTO Data to be updated
     * @return ResponseEntity with success or failure message
     */
    @Operation(summary = "Update User Data", description = "Updates the existing user data like personal information, health stats, etc.")
    @PutMapping
    public ResponseEntity<?> updateUserData(
            @Parameter(description = "Updated user data, including personal and health-related details.") @RequestBody UserDataDTO userDataDTO) {
        return userDataService.updateUserData(userDataDTO);
    }

    /**
     * Get user data
     * @return ResponseEntity with user data
     */
    @Operation(summary = "Get User Data", description = "Retrieves the user data for the authenticated user.")
    @GetMapping
    public ResponseEntity<?> getUserData() {
        return userDataService.getUserData();
    }
}
