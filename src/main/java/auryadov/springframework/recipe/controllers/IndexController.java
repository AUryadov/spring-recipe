package auryadov.springframework.recipe.controllers;

import auryadov.springframework.recipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        log.debug("IndexController.getIndexPage.start");
        model.addAttribute("recipes", recipeService.getRecipes());
        log.debug("IndexController.getIndexPage.start");

        return "index";
    }
}
