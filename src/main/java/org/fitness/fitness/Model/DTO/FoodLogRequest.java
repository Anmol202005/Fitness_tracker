package org.fitness.fitness.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodLogRequest {
    private String name;
    private String description;
}
