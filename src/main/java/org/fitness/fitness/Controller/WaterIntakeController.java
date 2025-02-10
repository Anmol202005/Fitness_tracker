package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Service.WaterIntakeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/water-intake")
@RequiredArgsConstructor
public class WaterIntakeController {

    private final WaterIntakeService waterIntakeService;

    @PostMapping("/log")
    public ResponseEntity<?> logWaterIntake(@RequestParam int amountInMl) {
        return waterIntakeService.logWaterIntake(amountInMl);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getWaterIntake(@RequestParam int i) {
        return waterIntakeService.getWaterIntakeByUser(i);
    }
}
