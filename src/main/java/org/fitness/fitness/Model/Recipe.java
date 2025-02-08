package org.fitness.fitness.Model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String calories;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private RecipeType recipeType;

    @Enumerated(EnumType.STRING)
    private FoodCategory foodCategory;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    private List<String> nutrients;

@ManyToMany
    @JoinTable(
        name = "recipe_ingredient",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )    private List<Ingredient> ingredients;

    @ElementCollection
    private List<String> steps;
}

