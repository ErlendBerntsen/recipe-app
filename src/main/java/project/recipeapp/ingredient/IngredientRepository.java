package project.recipeapp.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import project.recipeapp.ingredient.Ingredient;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);

}