package auryadov.springframework.recipe.services;

import auryadov.springframework.recipe.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
