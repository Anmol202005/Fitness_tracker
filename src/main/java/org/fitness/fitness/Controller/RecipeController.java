package org.fitness.fitness.Controller;

import org.fitness.fitness.Service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recommended")
    public ResponseEntity<?> getRecommendedRecipe() {
        return recipeService.getRecommendedRecipe();
    }

    @GetMapping
    public ResponseEntity<?> getAllRecipes() {
        return recipeService.getAllRecipies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        return recipeService.findRecipeById(id);
    }
}
