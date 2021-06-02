package project.recipeapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import project.recipeapp.units.Unit;
import project.recipeapp.units.volumes.CentiLiter;
import project.recipeapp.units.weights.Gram;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UnitRepository unitRepository;

    private IngredientModelAssembler ingredientAssembler;
    private IngredientController ingredientController;


    private Ingredient ingredient;
    private String name;
    private double price;
    private double amount;
    private Unit unit;
    private Category category;


    @BeforeEach
    void setup(){
        name = "Cointreau";
        price = 429.90;
        amount = 70;
        unit = new CentiLiter();
        unitRepository.save(unit);
        category = Category.ORANGE_LIQUEUR;
        ingredient = new Ingredient(name, price, amount, unit, category);
        ingredientAssembler =  new IngredientModelAssembler();
        ingredientController =  new IngredientController(ingredientRepository, ingredientAssembler);
    }

    @Test
    void ingredientShouldBeCreatedWithController() throws  Exception{
        ingredientController.newIngredient(ingredient);
        this.mockMvc.perform(get("/ingredients/" + ingredient.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void twoIngredientsShouldBeCreatedWithController() throws Exception{
        Unit gram = new Gram();
        unitRepository.save(gram);
        ingredientController.newIngredient(ingredient);
        ingredientController.newIngredient(new Ingredient("Sugar", 22.90, 1000, gram, Category.MISC));
        this.mockMvc.perform(get("/ingredients"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void responseShouldContainSelfLink() throws Exception{
        ingredientController.newIngredient(ingredient);
        String URI = "\"http://localhost/ingredients/" + ingredient.getId() + "\"";
        this.mockMvc.perform(get("/ingredients/" + ingredient.getId()))
                .andDo(print())
                .andExpect(content().string(containsString(URI)));
    }

    @Test
    void responseShouldContainAggregateRootLink() throws Exception{
        ingredientController.newIngredient(ingredient);
        String URI = "\"http://localhost/ingredients\"";
        this.mockMvc.perform(get("/ingredients/" + ingredient.getId()))
                .andDo(print())
                .andExpect(content().string(containsString(URI)));
    }


    @Test
    void ingredientNotFoundShouldHaveAppropriateErrorMessage(){
        Long id = -1L;
        String errorMessage = "Could not find ingredient " + id;
        try{
            ingredientController.one(id);
        }catch (IngredientNotFoundException e){
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    void ingredientShouldBeDeletedByController(){
        ingredientController.newIngredient(ingredient);
        assertTrue(ingredientRepository.findById(ingredient.getId()).isPresent());
        ingredientController.deleteIngredient(ingredient.getId());
        assertTrue(ingredientRepository.findById(ingredient.getId()).isEmpty());
    }

    @Test
    void ingredientShouldBeDeletedByRequest() throws Exception{
        ingredientController.newIngredient(ingredient);


        this.mockMvc.perform(get("/ingredients/" + ingredient.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(delete("/ingredients/" + ingredient.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/ingredients/" + ingredient.getId()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }



}
