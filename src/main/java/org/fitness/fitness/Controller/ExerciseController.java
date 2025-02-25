package org.fitness.fitness.Controller;

import lombok.RequiredArgsConstructor;
import org.fitness.fitness.Service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getExerciseById(@PathVariable Long id) {
        return exerciseService.getExerciseById(id);
    }

    @GetMapping("/bodyPart/{bodyPart}")
    public ResponseEntity<?> getExerciseByBodyPart(@PathVariable String bodyPart) {
        return exerciseService.getExerciseByBodyPart(bodyPart);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchExercises(@RequestParam String keyword) {
        return exerciseService.searchExercises(keyword);
    }
}
