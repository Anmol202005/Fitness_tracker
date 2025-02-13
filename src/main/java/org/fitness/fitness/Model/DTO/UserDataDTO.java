package org.fitness.fitness.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDataDTO {
    private String gender;
    private Integer age;
    private Integer height;
    private Integer weight;
    private String dietType;
    private String fitnessGoal;
    private String activityLevel;
    private Integer waterGoal;
    private Integer sleepGoal;
    private Integer calorieGoal;
}

