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

@RestController
@RequestMapping("/api/user-data")
@RequiredArgsConstructor
public class UserDataController {

    private final UserDataService userDataService;

    /**
     * Add user data
     * @param userDataDTO Data to be added
     * @return ResponseEntity with success or failure message
     */
    @PostMapping
    public ResponseEntity<?> addUserData(@RequestBody UserDataDTO userDataDTO) {
        return userDataService.addUserData(userDataDTO);
    }

    /**
     * Update user data
     * @param userDataDTO Data to be updated
     * @return ResponseEntity with success or failure message
     */
    @PutMapping
    public ResponseEntity<?> updateUserData(@RequestBody UserDataDTO userDataDTO) {
        return userDataService.updateUserData(userDataDTO);
    }

    /**
     * Get user data
     * @return ResponseEntity with user data
     */
    @GetMapping
    public ResponseEntity<?> getUserData() {
        return userDataService.getUserData();
    }
}
