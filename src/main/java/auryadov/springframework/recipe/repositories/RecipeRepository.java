package auryadov.springframework.recipe.repositories;

import auryadov.springframework.recipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
