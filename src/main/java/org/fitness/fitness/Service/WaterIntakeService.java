package org.fitness.fitness.Service;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Model.DTO.ResponseMessage;
import org.fitness.fitness.Model.DTO.WaterIntakeResponse;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Model.WaterIntake;
import org.fitness.fitness.Repository.WaterIntakeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterIntakeService {

    private final WaterIntakeRepository repository;

    public ResponseEntity<?> logWaterIntake(int amountInMl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        WaterIntake intake = new WaterIntake();
        intake.setAmountInMl(amountInMl);
        intake.setUserId(currentUser.getUserId());
        intake.setDate(LocalDate.now());
        repository.save(intake);
        return ResponseEntity.ok().body(ResponseMessage
                    .builder()
                    .message("Water Logged, Keep Drinking ")
                    .build());
    }


   public ResponseEntity<?> getWaterIntakeByUser(int i) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var currentUser = (User) authentication.getPrincipal();
    int water = 0;

    LocalDate today = LocalDate.now();

    if (i == 0) {
        List<WaterIntake> waterIntakes = repository.findByUserIdAndDate(currentUser.getUserId(), today);
        for (WaterIntake waterIntake : waterIntakes) {
            water += waterIntake.getAmountInMl();
        }
         return ResponseEntity.ok().body(WaterIntakeResponse
                    .builder()
                    .waterIntake(water)
                    .build());

    } else if (i == 1) {
        LocalDate weekStart = today.minusDays(6);
        List<WaterIntake> waterIntakes = repository.findByUserIdAndDateBetween(currentUser.getUserId(), weekStart, today);
        for (WaterIntake waterIntake : waterIntakes) {
            water += waterIntake.getAmountInMl();
        }
        return ResponseEntity.ok().body(WaterIntakeResponse
                    .builder()
                    .waterIntake(water)
                    .build());

    } else if (i == 2) {
        LocalDate monthStart = today.withDayOfMonth(1);
        List<WaterIntake> waterIntakes = repository.findByUserIdAndDateBetween(currentUser.getUserId(), monthStart, today);
        for (WaterIntake waterIntake : waterIntakes) {
            water += waterIntake.getAmountInMl();
        }
         return ResponseEntity.ok().body(WaterIntakeResponse
                    .builder()
                    .waterIntake(water)
                    .build());

    }
    else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessage
                    .builder()
                    .message("Invalid Input nigga")
                    .build());
    }
    }

}

