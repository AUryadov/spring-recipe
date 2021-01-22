package auryadov.springframework.recipe.controllers;

import auryadov.springframework.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;

    public IngredientController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String getIngredientList(Model model, @PathVariable Long recipeId) {
        log.debug("getIngredientList" + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipe/ingredients/list";
    }

}
