package auryadov.springframework.recipe.controllers;

import auryadov.springframework.recipe.commands.IngredientCommand;
import auryadov.springframework.recipe.services.IngredientService;
import auryadov.springframework.recipe.services.RecipeService;
import auryadov.springframework.recipe.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("recipe/")
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService,
                                IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("{recipeId}/ingredients")
    public String getIngredientList(Model model, @PathVariable Long recipeId) {
        log.debug("getIngredientList" + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipe/ingredients/list";
    }

    @GetMapping
    @RequestMapping("{recipeId}/ingredient/{ingredientId}/show")
    public String getIngredientById(Model model,
                                    @PathVariable Long recipeId,
                                    @PathVariable Long ingredientId) {
        log.debug("getIngredientById ---" + "ingredientId: " + ingredientId + ", recipeId: " + recipeId);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        return "recipe/ingredients/show";
    }

    @GetMapping
    @RequestMapping("{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable Long recipeId,
                                         @PathVariable Long ingredientId,
                                         Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        model.addAttribute("uomList", unitOfMeasureService.listAllUom());

        return "recipe/ingredients/ingredientform";
    }

    @PostMapping("{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        log.debug("saved recipe id: " + savedCommand.getRecipeId());
        log.debug("saved ingredient id: " + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

}
