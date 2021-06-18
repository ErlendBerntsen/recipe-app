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
import project.recipeapp.units.Unit;
import project.recipeapp.units.volumes.CentiLiter;
import project.recipeapp.units.volumes.Liter;
import project.recipeapp.units.volumes.MilliLiter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static project.recipeapp.units.Unit.convert;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipeTest {

    private Recipe recipe;
    private int portions;
    private String name;
    private String description;
    private String steps;
    private String glass;
    private double rating;
    private double difficulty;
    private String notes;
    private List<RecipeIngredient> ingredients;
    private Ingredient gin;
    private Ingredient lemonJuice;
    private Ingredient raspberrySyrup;
    private Ingredient eggWhite;



    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setup(){
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
        unitRepository.deleteAll();


        name = "Clover Club";
        portions = 1;
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
        unitRepository.save(milliLiter);
        unitRepository.save(centiLiter);
        unitRepository.save(liter);

        gin = new Ingredient("Beefeater London Dry", 489.90, 1, liter, Category.GIN);
        lemonJuice = new Ingredient("Lemon Juice", 20.50, 115, milliLiter, Category.JUICE);
        raspberrySyrup = new Ingredient("Raspberry Syrup", 20, 20, centiLiter, Category.JUICE);
        eggWhite = new Ingredient("Egg White", 5, 30, milliLiter , Category.FRIDGE_PANTRY);

        ingredientRepository.save(gin);
        ingredientRepository.save(lemonJuice);
        ingredientRepository.save(raspberrySyrup);
        ingredientRepository.save(eggWhite);

        ingredients = new ArrayList<>();
        RecipeIngredient ginIngredient = new RecipeIngredient("Gin", gin, 45, milliLiter,false);
        RecipeIngredient lemonJuiceIngredient = new RecipeIngredient("Lemon Juice", lemonJuice, 22.5, milliLiter,false);
        RecipeIngredient raspberrySyrupIngredient = new RecipeIngredient("Raspberry Syrup", raspberrySyrup, 22.5, milliLiter,false);
        RecipeIngredient eggWhiteIngredient = new RecipeIngredient("Egg White", eggWhite, 15, milliLiter,false);
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
                + "Portions: " + portions +"\n"
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
        assertEquals(format, recipe.toString());
    }

    @Test
    void recipeShouldCalculatePriceFromIngredients(){
        double price = 0;
        price += gin.getPrice() * ((45.0/1000.0)/ 1.0);
        price += lemonJuice.getPrice() * ((22.5/1.0)/ 115.0);
        price += raspberrySyrup.getPrice() * ((22.5/10.0)/ 20.0);
        price += eggWhite.getPrice() * ((15.0/1.0)/ 30.0);
        assertEquals(Math.round(price), recipe.getPrice());
    }
}
