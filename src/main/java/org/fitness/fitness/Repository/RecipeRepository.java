package org.fitness.fitness.Repository;

import java.util.List;

import org.fitness.fitness.Model.DietType;
import org.fitness.fitness.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByFoodCategory(DietType category);
}
