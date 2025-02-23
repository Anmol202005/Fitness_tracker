package org.fitness.fitness.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long foodLogId;

    @ManyToOne
    @JoinColumn(name = "user_data_id")
    private User user;
    private MealType mealType;
    private String foodName;
    private String calories;
    private String description;
    private LocalDateTime logTime = LocalDateTime.now();
    private LocalDate date = LocalDate.now();
}
