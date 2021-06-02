package project.recipeapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.recipeapp.units.volumes.*;
import project.recipeapp.units.Unit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IngredientTest {


    @Autowired
    private IngredientRepository ingredientRepository;


    @Autowired
    private UnitRepository unitRepository;

    private Ingredient ingredient;
    private String name;
    private double price;
    private double amount;
    private Unit unit;
    private Category category;


    @BeforeEach
    void setup(){
        name = "Absolut Vodka";
        price = 464.90;
        amount = 1;
        unit = new Liter();
        unitRepository.save(unit);
        category = Category.VODKA;
        ingredient = new Ingredient(name, price, amount, unit, category);
        ingredientRepository.save(ingredient);
    }

    @Test
    void ingredientShouldBeCreatedWithRepository(){
        var ingredients = ingredientRepository.findAll();
        var savedIngredient = ingredients.get(0);
        assertEquals(savedIngredient, ingredient);
    }

    @Test
    void twoIngredientsShouldBeSavedInRepository(){
        Ingredient ingredient2 = new Ingredient ("Absolut Vanilla Vodka", price, amount, unit, category);
        ingredientRepository.save(ingredient2);
        var numberOfIngredients = ingredientRepository.count();
        assertEquals(2, numberOfIngredients);
    }

    @Test
    void ingredientsWithdifferentUnitsAndCategoriesShouldBeSavedInRepository(){
        Unit centiliter = new CentiLiter();
        unitRepository.save(centiliter);
        Category rumCategory = Category.RUM;
        String capMorg = "Captain Morgan White Rum";
        Ingredient ingredient2 = new Ingredient(capMorg, 325.90,70, centiliter, rumCategory);
        ingredientRepository.save(ingredient);
        ingredientRepository.save(ingredient2);
        var ing1 = ingredientRepository.findByName(name);
        var ing2 = ingredientRepository.findByName(capMorg);
        assertTrue(ing1.isPresent() && ing2.isPresent());
    }

    @Test
    void ingredientShouldBePrintedCorrectly(){
        String correctFormat = "Name = Absolut Vodka, Price = 464.9 kr, Quantity = 1.0L, Category = Vodka";
        assertEquals(correctFormat, ingredient.toString());
    }
}
