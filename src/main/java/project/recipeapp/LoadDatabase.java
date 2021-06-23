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
import project.recipeapp.units.Piece;
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
            Unit piece = new Piece();
            unitRepository.save(liter);
            unitRepository.save(centiliter);
            unitRepository.save(milliliter);
            unitRepository.save(gram);
            unitRepository.save(piece);

            unitRepository.findAll().forEach(unit -> {
                log.info("Preloaded unit: " + unit);
            });

            var vodka = new Ingredient("Absolut Vodka", 464.90, 1, liter, Category.VODKA);
            var cranberryJuice = new Ingredient("Cranberry Drikk", 46.90, 1, liter, Category.JUICE);
            var grapefruitJuice = new Ingredient("Cevita Grapefruktjuice", 45.90, 1, liter, Category.JUICE);
            var grapefruit = new Ingredient("Grapefruit", 16.90, 1, piece, Category.FRIDGE_PANTRY);
            ingredientRepository.save(new Ingredient("Captain Morgan White Rum", 325.90, 70, centiliter, Category.RUM));
            ingredientRepository.save(new Ingredient("Sugar", 22.90, 1000, gram, Category.MISC));
            ingredientRepository.save(vodka);
            ingredientRepository.save(cranberryJuice);
            ingredientRepository.save(grapefruitJuice);
            ingredientRepository.save(grapefruit);

            ingredientRepository.findAll().forEach(ingredient -> {
                log.info("Preloaded ingredient: " + ingredient);
            });

            List<RecipeIngredient> ingredients = new ArrayList<>();
            ingredients.add(new RecipeIngredient("Vodka", vodka, 60, milliliter, false));
            ingredients.add(new RecipeIngredient("Cranberry Juice", cranberryJuice, 90, milliliter, false));
            ingredients.add(new RecipeIngredient("Grapefruit Juice", grapefruitJuice, 60, milliliter, false));
            ingredients.add(new RecipeIngredient("Grapefruit", grapefruit, 1, piece, true));


            var seabreeze = new Recipe("Sea Breeze", ingredients);
            seabreeze.setDescription("A simple, tart, and bitter vodka mixed drink");
            seabreeze.setSteps("1. Combine all ingredients to your cocktail shaker\n" +
                    "2. Shake with ice" +
                    "3. Strain over fresh ice into a collins glass" +
                    "4. Garnish with a slice of grapefruit");
            seabreeze.setNotes("Too bitter from the grapefruit juice");
            seabreeze.setGlass("Collins");
            seabreeze.setRating(6);
            seabreeze.setRating(3);
            recipeRepository.save(seabreeze);


            recipeRepository.findAll().forEach(recipe -> {
                log.info("Preloaded recipe: " + recipe);
            });
        };
    }


}

