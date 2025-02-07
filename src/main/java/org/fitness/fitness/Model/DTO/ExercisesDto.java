package org.fitness.fitness.Model.DTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ExercisesDto {
    List<?> exercises;
}
