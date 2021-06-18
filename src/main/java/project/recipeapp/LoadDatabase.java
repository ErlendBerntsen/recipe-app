package project.recipeapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.recipeapp.ingredient.Ingredient;
import project.recipeapp.ingredient.IngredientRepository;
import project.recipeapp.recipe.Recipe;
import project.recipeapp.recipe.RecipeIngredient;
import project.recipeapp.recipe.RecipeRepository;
import project.recipeapp.units.Unit;
import project.recipeapp.units.volumes.CentiLiter;
import project.recipeapp.units.volumes.Liter;
import project.recipeapp.units.volumes.MilliLiter;
import project.recipeapp.units.weights.Gram;

import java.util.ArrayList;
import java.util.List;



@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);


    @Bean
    CommandLineRunner initDatabase(RecipeRepository recipeRepository,
                                   IngredientRepository ingredientRepository,
                                   UnitRepository unitRepository){
        return args -> {
            Unit liter = new Liter();
            Unit centiliter = new CentiLiter();
            Unit milliliter = new MilliLiter();
            Unit gram = new Gram();
            unitRepository.save(liter);
            unitRepository.save(centiliter);
            unitRepository.save(milliliter);
            unitRepository.save(gram);

            unitRepository.findAll().forEach(unit -> {
                log.info("Preloaded " + unit);
            });

            Ingredient vodka = new Ingredient("Absolut Vodka", 464.90, 1, liter, Category.VODKA);
            Ingredient cranberryJuice = new Ingredient("Cranberry Drikk", 46.90, 1, liter, Category.JUICE);
            Ingredient grapefruitJuice = new Ingredient("Cevita Grapefruktjuice", 45.90, 1, liter, Category.JUICE);
            ingredientRepository.save(new Ingredient("Captain Morgan White Rum", 325.90, 70, centiliter, Category.RUM));
            ingredientRepository.save(new Ingredient("Sugar", 22.90, 1000, gram, Category.MISC));
            ingredientRepository.save(vodka);
            ingredientRepository.save(cranberryJuice);
            ingredientRepository.save(grapefruitJuice);

            ingredientRepository.findAll().forEach(ingredient -> {
                log.info("Preloaded " + ingredient);
            });

            List<RecipeIngredient> ingredients = new ArrayList<>();
            ingredients.add(new RecipeIngredient("Vodka", vodka, 60, milliliter, false));
            ingredients.add(new RecipeIngredient("Cranberry Juice", cranberryJuice, 90, milliliter, false));
            ingredients.add(new RecipeIngredient("Grapefruit Juice", grapefruitJuice, 60, milliliter, false));

            recipeRepository.save(new Recipe("Sea Breeze", ingredients));

            recipeRepository.findAll().forEach(recipe -> {
                log.info("Preloaded " + recipe);
            });
        };
    }


}

