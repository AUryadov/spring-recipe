package auryadov.springframework.recipe.services;

import auryadov.springframework.recipe.commands.IngredientCommand;
import auryadov.springframework.recipe.converters.IngredientCommandToIngredient;
import auryadov.springframework.recipe.converters.IngredientToIngredientCommand;
import auryadov.springframework.recipe.domain.Ingredient;
import auryadov.springframework.recipe.domain.Recipe;
import auryadov.springframework.recipe.repositories.RecipeRepository;
import auryadov.springframework.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImp implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImp(IngredientToIngredientCommand ingredientToIngredientCommand,
                                RecipeRepository recipeRepository,
                                UnitOfMeasureRepository unitOfMeasureRepository,
                                IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) {
            // todo impl error handling
            log.error("recipe not found with id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert).findFirst();

        if (ingredientCommandOptional.isEmpty()) {
            // todo impl error handling
            log.error("ingredient not found with id: " + ingredientId);
        }

        return ingredientCommandOptional.orElseThrow(RuntimeException::new);
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (recipeOptional.isEmpty()) {
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                .findById(command.getUom().getId())
                .orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
            } else {
                // add new ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe recipeSaved = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = recipeSaved.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            // check by description
            if (savedIngredientOptional.isEmpty()) {
                // not totally safe... but best guess
                savedIngredientOptional = recipeSaved.getIngredients().stream()
                        .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                        .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                        .filter(ingredient -> ingredient.getUnitOfMeasure().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            // to do check for fail
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        log.debug("deleting ingredient id: " + ingredientId);

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();
            log.debug("recipe found");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                log.debug("found ingredient");
                Ingredient ingredientToDelete = ingredientOptional.get();
                ingredientToDelete.setRecipe(null);
                recipe.getIngredients().remove(ingredientToDelete);
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe is not found id: " + recipeId);
        }
    }
}
