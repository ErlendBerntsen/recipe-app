package project.recipeapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.recipeapp.ingredient.Ingredient;
import project.recipeapp.ingredient.IngredientRepository;
import project.recipeapp.recipe.Recipe;
import project.recipeapp.recipe.RecipeIngredient;
import project.recipeapp.recipe.RecipeRepository;
import project.recipeapp.units.volumes.CentiLiter;
import project.recipeapp.units.volumes.Liter;
import project.recipeapp.units.volumes.MilliLiter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipeTest {

    private Recipe recipe;
    private String name;
    private String description;
    private String steps;
    private String glass;
    private double price;
    private double rating;
    private double difficulty;
    private String notes;
    private List<RecipeIngredient> ingredients;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setup(){
        name = "Clover Club";
        steps = "\t1. Combine all the ingredients into your cocktail\n"
                + "\t2. Dry shake (without ice)\n"
                + "\t3. Wet shake (with ice)\n"
                + "\t4. Strain into a chilled coupe\n"
                + "\t5. Garnish with skewered raspberries";
        description = "A sour and sweet cocktail from the early 1900's with taste of lemon and raspberries.";
        glass = "Coupe";
        rating = 7;
        difficulty = 6;
        notes = "Too much gin taste, and needs a little less egg whites";



        MilliLiter milliLiter = new MilliLiter();
        CentiLiter centiLiter = new CentiLiter();
        Liter liter = new Liter();
        unitRepository.deleteAll();
        unitRepository.save(milliLiter);
        unitRepository.save(centiLiter);
        unitRepository.save(liter);

        Ingredient gin = new Ingredient("Gin", 489.90, 1, liter, Category.GIN);
        Ingredient lemonJuice = new Ingredient("Lemon Juice", 20.50, 115, milliLiter, Category.JUICE);
        Ingredient raspberrySyrup = new Ingredient("Raspberry Syrup", 20, 20, centiLiter, Category.JUICE);
        Ingredient eggWhite = new Ingredient("Egg White", 5, 30, milliLiter , Category.FRIDGE_PANTRY);

        ingredientRepository.deleteAll();
        ingredientRepository.save(gin);
        ingredientRepository.save(lemonJuice);
        ingredientRepository.save(raspberrySyrup);
        ingredientRepository.save(eggWhite);

        ingredients = new ArrayList<>();
        RecipeIngredient ginIngredient = new RecipeIngredient(gin, 45, milliLiter);
        RecipeIngredient lemonJuiceIngredient = new RecipeIngredient(lemonJuice, 22.5, milliLiter);
        RecipeIngredient raspberrySyrupIngredient = new RecipeIngredient(raspberrySyrup, 22.5, milliLiter);
        RecipeIngredient eggWhiteIngredient = new RecipeIngredient(eggWhite, 15, milliLiter);
        ingredients.add(ginIngredient);
        ingredients.add(lemonJuiceIngredient);
        ingredients.add(raspberrySyrupIngredient);
        ingredients.add(eggWhiteIngredient);

        recipe = new Recipe(name, ingredients);

    }

    @Test
    void recipeShouldBeSavedInRepository(){
        recipeRepository.save(recipe);
        var recipeFound = recipeRepository.findByNameIgnoreCase(name);
        assertTrue(recipeFound.isPresent());
        assertEquals(recipe, recipeFound.get());
    }

    @Test
    void twoRecipesShouldBeSavedInRepository(){
        recipeRepository.save(recipe);
        recipeRepository.save(new Recipe("recipe 2", new ArrayList<>()));
        assertEquals(2, recipeRepository.count());
    }

    @Test
    void recipeShouldBePrintedCorrectly(){
        String format = "Name: " + name + "\n"
                + "Description: " + description + "\n"
                + "Steps:\n" + steps + "\n"
                + "Notes: " + notes + "\n"
                + "Glass: " + glass + "\n"
                + "Rating: " + rating + "/10\n"
                + "Difficulty: " + difficulty + "/10\n"
                + "Ingredients: " + ingredients.toString();

        recipe.setDescription(description);
        recipe.setSteps(steps);
        recipe.setNotes(notes);
        recipe.setGlass(glass);
        recipe.setRating(rating);
        recipe.setDifficulty(difficulty);
        System.out.println(recipe.toString());
        assertEquals(format, recipe.toString());
    }
}
