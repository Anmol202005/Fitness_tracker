package org.fitness.fitness.Service;

import java.util.Locale;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.ActivityLevel;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.DTO.UserDataDTO;
import org.fitness.fitness.Model.DietType;
import org.fitness.fitness.Model.FitnessGoal;
import org.fitness.fitness.Model.Gender;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Model.UserData;
import org.fitness.fitness.Repository.UserDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDataService {
    private final UserDataRepository userDataRepository;

    public ResponseEntity<?> addUserData(UserDataDTO userDataDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        UserData userData = new UserData();
        userData.setUser(currentUser);
        userData.setAge(userDataDTO.getAge());
        userData.setGender(Gender.valueOf(userDataDTO
                .getGender().toUpperCase(Locale.ROOT)));
        userData.setHeight(userDataDTO.getHeight());
        userData.setWeight(userDataDTO.getWeight());
        userData.setActivityLevel(ActivityLevel.valueOf(userDataDTO
                .getActivityLevel().toUpperCase(Locale.ROOT)));
        userData.setDietType(DietType.valueOf(userDataDTO
                .getDietType().toUpperCase(Locale.ROOT)));
        userData.setSleepGoal(userDataDTO.getSleepGoal());
        userData.setFitnessGoal(FitnessGoal.valueOf(userDataDTO
                .getFitnessGoal().toUpperCase(Locale.ROOT)));
        userData.setStepGoal(userDataDTO.getStepGoal());
        userData.setWaterGoal(userDataDTO.getWaterGoal());

        userDataRepository.save(userData);

        return ResponseEntity.ok().body(ResponseMessage
                    .builder()
                    .message("User Data successfully added")
                    .build());
    }

    public ResponseEntity<?> updateUserData(UserDataDTO userDataDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        if(!userDataRepository.existsByUser(currentUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("User data does not exist")
                    .build());
        }
        UserData userData = userDataRepository.getByUser(currentUser);
        if (userDataDTO.getAge() != null) {
            userData.setAge(userDataDTO.getAge());
        }
        if (userDataDTO.getGender() != null) {
            userData.setGender(Gender.valueOf(userDataDTO.getGender().toUpperCase(Locale.ROOT)));
        }
        if (userDataDTO.getHeight() != null) {
            userData.setHeight(userDataDTO.getHeight());
        }
        if (userDataDTO.getWeight() != null) {
            userData.setWeight(userDataDTO.getWeight());
        }
        if (userDataDTO.getActivityLevel() != null) {
            userData.setActivityLevel(ActivityLevel.valueOf(userDataDTO.getActivityLevel().toUpperCase(Locale.ROOT)));
        }
        if (userDataDTO.getDietType() != null) {
            userData.setDietType(DietType.valueOf(userDataDTO.getDietType().toUpperCase(Locale.ROOT)));
        }
        if (userDataDTO.getSleepGoal() != null) {
            userData.setSleepGoal(userDataDTO.getSleepGoal());
        }
        if (userDataDTO.getFitnessGoal() != null) {
            userData.setFitnessGoal(FitnessGoal.valueOf(userDataDTO.getFitnessGoal().toUpperCase(Locale.ROOT)));
        }
        if (userDataDTO.getStepGoal() != null) {
            userData.setStepGoal(userDataDTO.getStepGoal());
        }
        if (userDataDTO.getWaterGoal() != null) {
            userData.setWaterGoal(userDataDTO.getWaterGoal());
        }

        userDataRepository.save(userData);

        return ResponseEntity.ok().body(ResponseMessage
                    .builder()
                    .message("User Data updated added")
                    .build());

    }

    public ResponseEntity<?> getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        var userData = userDataRepository.getByUser(currentUser);
        return ResponseEntity.ok().body(userData);
    }
}
