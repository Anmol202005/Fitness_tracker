package org.fitness.fitness.Service;

import java.util.List;

import org.fitness.fitness.Model.FoodCategory;
import org.fitness.fitness.Model.Recipe;
import org.fitness.fitness.Model.User;
import org.fitness.fitness.Repository.RecipeRepository;
import org.fitness.fitness.Repository.UserDataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    private final UserDataRepository userDataRepository;
    private final RecipeRepository recipeRepository;

    public RecipeService(UserDataRepository userDataRepository, RecipeRepository recipeRepository) {
        this.userDataRepository = userDataRepository;
        this.recipeRepository = recipeRepository;
    }

    public ResponseEntity<?> getRecommendedRecipe(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();
        FoodCategory category = userDataRepository.getByUser(currentUser).getFoodCategory();
        List<Recipe> recipes = recipeRepository.findByFoodCategory(category);
        return ResponseEntity.ok(recipes);
    }

    public ResponseEntity<?> getAllRecipies(){
        List<Recipe> recipes = recipeRepository.findAll();
        return ResponseEntity.ok(recipes);
    }

    public ResponseEntity<?> findRecipeById(Long id){
        Recipe recipe = recipeRepository.findById(id).get();
        return ResponseEntity.ok(recipe);
    }



}
