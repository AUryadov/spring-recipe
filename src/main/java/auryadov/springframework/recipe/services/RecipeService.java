package auryadov.springframework.recipe.services;

import auryadov.springframework.recipe.commands.RecipeCommand;
import auryadov.springframework.recipe.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(Long id);
    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);

    RecipeCommand findCommandById(Long id);

    void deleteById(Long id);
}
