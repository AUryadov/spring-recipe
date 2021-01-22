package auryadov.springframework.recipe.bootstrap;

import auryadov.springframework.recipe.domain.*;
import auryadov.springframework.recipe.repositories.CategoryRepository;
import auryadov.springframework.recipe.repositories.RecipeRepository;
import auryadov.springframework.recipe.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>(2);

        // Get UOMs
        Optional<UnitOfMeasure> eachUomOptional = unitOfMeasureRepository.findByDescription("Each");

        if (eachUomOptional.isEmpty()) {
            throw new RuntimeException("Expected UOM is Not Found");
        }

        Optional<UnitOfMeasure> tableSpoonUomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");

        if (tableSpoonUomOptional.isEmpty()) {
            throw new RuntimeException("Expected UOM is Not Found");
        }

        Optional<UnitOfMeasure> teaspoonUomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");

        if (teaspoonUomOptional.isEmpty()) {
            throw new RuntimeException("Expected UOM is Not Found");
        }

        Optional<UnitOfMeasure> dashUomOptional = unitOfMeasureRepository.findByDescription("Dash");

        if (dashUomOptional.isEmpty()) {
            throw new RuntimeException("Expected UOM is Not Found");
        }

        Optional<UnitOfMeasure> pinchUomOptional = unitOfMeasureRepository.findByDescription("Pinch");

        if (pinchUomOptional.isEmpty()) {
            throw new RuntimeException("Expected UOM is Not Found");
        }

        Optional<UnitOfMeasure> cupUomOptional = unitOfMeasureRepository.findByDescription("Cup");

        if (cupUomOptional.isEmpty()) {
            throw new RuntimeException("Expected UOM is Not Found");
        }

        // get optionals
        UnitOfMeasure eachUom = eachUomOptional.get();
        UnitOfMeasure tableSpoon = tableSpoonUomOptional.get();
        UnitOfMeasure teaspoonUom = teaspoonUomOptional.get();
        UnitOfMeasure dashUom = dashUomOptional.get();
        UnitOfMeasure pinchUom = pinchUomOptional.get();
        UnitOfMeasure cupUom = cupUomOptional.get();

        // Get categories
        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");

        if (americanCategoryOptional.isEmpty()) {
            throw new RuntimeException("Expected Category is Not Found");
        }

        Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");

        if (mexicanCategoryOptional.isEmpty()) {
            throw new RuntimeException("Expected Category is Not Found");
        }

        Category americanCategory = americanCategoryOptional.get();
        Category mexicanCategory = mexicanCategoryOptional.get();

        // Yummi guac
        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect guacomole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("Direction");

        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("Notes for recipe");

        guacRecipe.setNotes(guacNotes);

        guacRecipe.addIngredient(new Ingredient("avocado", new BigDecimal(2), eachUom));
        guacRecipe.addIngredient(new Ingredient("avocado", new BigDecimal(2), tableSpoon));
        guacRecipe.addIngredient(new Ingredient("avocado", new BigDecimal(2), tableSpoon));
        guacRecipe.addIngredient(new Ingredient("avocado", new BigDecimal(2), pinchUom));
        guacRecipe.addIngredient(new Ingredient("avocado", new BigDecimal(2), dashUom));

        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);

        // Yummi guac2
        Recipe guacRecipe1 = new Recipe();
        guacRecipe1.setDescription("Perfect guacomole 2");
        guacRecipe1.setPrepTime(10);
        guacRecipe1.setCookTime(0);
        guacRecipe1.setDifficulty(Difficulty.EASY);
        guacRecipe1.setDirections("Direction");

        Notes guacNotes1 = new Notes();
        guacNotes1.setRecipeNotes("Notes for recipe1");

        guacRecipe1.setNotes(guacNotes);

        guacRecipe1.addIngredient(new Ingredient("avocado", new BigDecimal(2), eachUom));
        guacRecipe1.addIngredient(new Ingredient("avocado", new BigDecimal(2), eachUom));
        guacRecipe1.addIngredient(new Ingredient("avocado", new BigDecimal(2), tableSpoon));
        guacRecipe1.addIngredient(new Ingredient("avocado", new BigDecimal(2), pinchUom));
        guacRecipe1.addIngredient(new Ingredient("avocado", new BigDecimal(2), dashUom));

        guacRecipe1.getCategories().add(americanCategory);
        guacRecipe1.getCategories().add(mexicanCategory);

        recipes.add(guacRecipe);
        recipes.add(guacRecipe1);

        return recipes;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("IndexController.getIndexPage.start");
        recipeRepository.saveAll(getRecipes());
        log.debug("IndexController.getIndexPage.start");
    }
}
