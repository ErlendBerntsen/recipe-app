package project.recipeapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.recipeapp.units.Unit;
import project.recipeapp.units.volumes.CentiLiter;
import project.recipeapp.units.volumes.Liter;
import project.recipeapp.units.weights.Gram;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IngredientControllerTest {

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
        ingredientController =  new IngredientController(ingredientRepository, unitRepository, ingredientAssembler);
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

    void ingredientShouldBeCreatedByRequest()throws Exception{
        String unitName = "Centiliter";
        String jname = "\"name\":\"" + name + "\"";
        String jprice = ",\"price\":" + price;
        String jamount = ",\"amount\":" + amount;
        String junit =  ",\"unit\":{\"name\":\"" + unitName
                + "\",\"abbreviation\":\"" + "cl"
                +  "\", \"unitType\":\"" + unitName + "\"}";
        String jcategory =",\"category\":\"" + category.name() + "\"";
        String jsonEdit = "{" + jname + jprice + jamount + junit + jcategory + "}";

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/ingredients/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonEdit);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(jsonEdit)));
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


    @Test
    void ingredientShouldBeEditedByController(){
        ingredientController.newIngredient(ingredient);
        String newName = "new name";
        double newPrice = 100;
        Map<String, Object> newFields = new HashMap<>();
        newFields.put("name", newName);
        newFields.put("price", newPrice);


        assertTrue(ingredientRepository.findById(ingredient.getId()).isPresent());
        assertNotEquals(newName, ingredientRepository.findById(ingredient.getId()).get().getName());
        assertNotEquals(newPrice, ingredientRepository.findById(ingredient.getId()).get().getPrice());

        ingredientController.editIngredient(ingredient.getId(), newFields);

        assertTrue(ingredientRepository.findById(ingredient.getId()).isPresent());
        assertEquals(newName, ingredientRepository.findById(ingredient.getId()).get().getName());
        assertEquals(newPrice, ingredientRepository.findById(ingredient.getId()).get().getPrice());
    }

    @Test
    void ingredientChangeEveryFieldByController(){
        ingredientController.newIngredient(ingredient);
        String newName = "new name";
        double newPrice = 100;
        double newAmount = 1;
        Unit newUnit = new Liter();
        Map<String, String> map = new HashMap<>();
        map.put("name", newUnit.getName());
        Category newCategory = Category.VODKA;

        Map<String, Object> newFields = new HashMap<>();
        newFields.put("name", newName);
        newFields.put("price", newPrice);
        newFields.put("amount", newAmount);
        newFields.put("unit", map);
        newFields.put("category", newCategory.name());


        assertTrue(ingredientRepository.findById(ingredient.getId()).isPresent());
        assertNotEquals(newName, ingredientRepository.findById(ingredient.getId()).get().getName());
        assertNotEquals(newPrice, ingredientRepository.findById(ingredient.getId()).get().getPrice());
        assertNotEquals(newAmount, ingredientRepository.findById(ingredient.getId()).get().getAmount());
        assertNotEquals(newUnit.getName(), ingredientRepository.findById(ingredient.getId()).get().getUnit().getName());
        assertNotEquals(newCategory, ingredientRepository.findById(ingredient.getId()).get().getCategory());

        ingredientController.editIngredient(ingredient.getId(), newFields);

        assertTrue(ingredientRepository.findById(ingredient.getId()).isPresent());
        assertEquals(newName, ingredientRepository.findById(ingredient.getId()).get().getName());
        assertEquals(newPrice, ingredientRepository.findById(ingredient.getId()).get().getPrice());
        assertEquals(newAmount, ingredientRepository.findById(ingredient.getId()).get().getAmount());
        assertEquals(newUnit.getName(), ingredientRepository.findById(ingredient.getId()).get().getUnit().getName());
        assertEquals(newCategory, ingredientRepository.findById(ingredient.getId()).get().getCategory());
    }



    @Test
    void ingredientChangeEveryFieldByRequest()throws Exception{
        ingredientController.newIngredient(ingredient);
        String newName = "new name";
        double newPrice = 100;
        double newAmount = 1;
        Unit newUnit = new Liter();
        Category newCategory = Category.VODKA;

        String jname = "\"name\":\"" + newName + "\"";
        String jprice = ",\"price\":" + newPrice;
        String jamount = ",\"amount\":" + newAmount;
        String junit =  ",\"unit\":{\"name\":\"" + newUnit.getName()
                + "\",\"abbreviation\":\"" + newUnit.getAbbreviation() +  "\"}";
        String jcategory =",\"category\":\"" + newCategory.name() + "\"";
        String jsonEdit = "{" + jname + jprice + jamount + junit + jcategory + "}";

        assertTrue(ingredientRepository.findById(ingredient.getId()).isPresent());
        assertNotEquals(newName, ingredientRepository.findById(ingredient.getId()).get().getName());
        assertNotEquals(newPrice, ingredientRepository.findById(ingredient.getId()).get().getPrice());
        assertNotEquals(newAmount, ingredientRepository.findById(ingredient.getId()).get().getAmount());
        assertNotEquals(newUnit.getName(), ingredientRepository.findById(ingredient.getId()).get().getUnit().getName());
        assertNotEquals(newCategory, ingredientRepository.findById(ingredient.getId()).get().getCategory());



        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/ingredients/" + ingredient.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonEdit);


        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(jname)))
                .andExpect(content().string(containsString(jprice)))
                .andExpect(content().string(containsString(jamount)))
                .andExpect(content().string(containsString(jcategory)));
    }

}
