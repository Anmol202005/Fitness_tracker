package org.fitness.fitness.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userid;
    @OneToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    private Gender gender;
    private Integer age;
    private Integer height;
    private Integer weight;
    private DietType dietType;
    private FitnessGoal fitnessGoal;
    private ActivityLevel activityLevel;
    private Integer waterGoal;
    private Integer sleepGoal;
    private Integer stepGoal;
}
