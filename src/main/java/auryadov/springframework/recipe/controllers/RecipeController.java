package auryadov.springframework.recipe.controllers;

import auryadov.springframework.recipe.commands.RecipeCommand;
import auryadov.springframework.recipe.domain.Recipe;
import auryadov.springframework.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/recipe")
public class RecipeController {

    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("{id}/show")
    public String getRecipeById(Model model, @PathVariable Long id) {
        Recipe recipe = recipeService.findById(id);

        model.addAttribute("recipe", recipe);

        return "recipe/show";
    }

    @GetMapping("/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable Long id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));

        return "recipe/recipeform";
    }

    @PostMapping("")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        log.debug(command.toString());
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable Long id) {
        log.debug("deleteById.start");
        recipeService.deleteById(id);
        log.debug("deleteById.end");

        return "redirect:/";
    }
}
