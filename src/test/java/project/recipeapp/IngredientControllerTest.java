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
import project.recipeapp.ingredient.*;
import project.recipeapp.units.Unit;
import project.recipeapp.units.volumes.Liter;

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


    private IngredientDTO ingredient;
    private String name;
    private double price;
    private double amount;
    private String unit;
    private Category category;
    private Long id;


    @BeforeEach
    void setup(){
        name = "Cointreau";
        price = 429.90;
        amount = 70;
        unit = "Centiliter"; //Centiliter is created in LoadDatabase.java
        category = Category.ORANGE_LIQUEUR;
        ingredient = new IngredientDTO(name, price, amount, unit , category);
        ingredientAssembler =  new IngredientModelAssembler();
        ingredientController = new IngredientController(ingredientRepository, unitRepository, ingredientAssembler);
        ingredientRepository.deleteAll();
        ingredientController.newIngredient(ingredient);
        id = ingredientRepository.findByNameIgnoreCase(ingredient.getName()).get().getId();
    }


    @Test
    void ingredientShouldBeCreatedWithController() throws  Exception{
        long nrOfIngredients = ingredientRepository.count();
        ingredientController.newIngredient(ingredient);
        this.mockMvc.perform(get("/ingredients/" ))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(nrOfIngredients + 1, ingredientRepository.count());
    }

    @Test
    void twoIngredientsShouldBeCreatedWithController() throws Exception{
        //Gram is created in LoadDatabase.java
        ingredientController.newIngredient(new IngredientDTO("Sugar", 22.90, 1000, "Gram", Category.MISC));
        this.mockMvc.perform(get("/ingredients"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void ingredientShouldBeCreatedByRequest()throws Exception{
        String jname = "\"name\":\"" + name + "\"";
        String jprice = ",\"price\":" + price;
        String jamount = ",\"amount\":" + amount;
        String junit =  ",\"unit\":\"" + unit + "\"";
        String jcategory =",\"category\":\"" + category.name() + "\"";
        String jsonEdit = "{" + jname + jprice + jamount + junit + jcategory + "}";
        String junitResponse = ",\"unit\":{\"name\":\"" + unit + "\",\"abbreviation\":\"cl\"}";

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/ingredients/")
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
                .andExpect(content().string(containsString(junitResponse)))
                .andExpect(content().string(containsString(jcategory)));
    }

    @Test
    void responseShouldContainSelfLink() throws Exception{
        String URI = "\"http://localhost/ingredients/" + id + "\"";
        this.mockMvc.perform(get("/ingredients/" + id))
                .andDo(print())
                .andExpect(content().string(containsString(URI)));
    }

    @Test
    void responseShouldContainAggregateRootLink() throws Exception{
        String URI = "\"http://localhost/ingredients\"";
        this.mockMvc.perform(get("/ingredients/" + id))
                .andDo(print())
                .andExpect(content().string(containsString(URI)));
    }


    @Test
    void ingredientNotFoundShouldHaveAppropriateErrorMessage(){
        Long badId = -1L;
        String errorMessage = "Could not find ingredient " + badId;
        try{
            ingredientController.one(badId);
        }catch (IngredientNotFoundException e){
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    void ingredientShouldBeDeletedByController(){
        assertTrue(ingredientRepository.findById(id).isPresent());
        ingredientController.deleteIngredient(id);
        assertTrue(ingredientRepository.findById(id).isEmpty());
    }

    @Test
    void ingredientShouldBeDeletedByRequest() throws Exception{
        this.mockMvc.perform(get("/ingredients/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(delete("/ingredients/" + id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/ingredients/" + id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    void ingredientShouldBeEditedByController(){
        String newName = "new name";
        double newPrice = 100;
        Map<String, Object> newFields = new HashMap<>();
        newFields.put("name", newName);
        newFields.put("price", newPrice);


        assertTrue(ingredientRepository.findById(id).isPresent());
        assertNotEquals(newName, ingredientRepository.findById(id).get().getName());
        assertNotEquals(newPrice, ingredientRepository.findById(id).get().getPrice());

        ingredientController.editIngredient(id, newFields);

        assertTrue(ingredientRepository.findById(id).isPresent());
        assertEquals(newName, ingredientRepository.findById(id).get().getName());
        assertEquals(newPrice, ingredientRepository.findById(id).get().getPrice());
    }

    @Test
    void ingredientChangeEveryFieldByController(){
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


        assertTrue(ingredientRepository.findById(id).isPresent());
        assertNotEquals(newName, ingredientRepository.findById(id).get().getName());
        assertNotEquals(newPrice, ingredientRepository.findById(id).get().getPrice());
        assertNotEquals(newAmount, ingredientRepository.findById(id).get().getAmount());
        assertNotEquals(newUnit.getName(), ingredientRepository.findById(id).get().getUnit().getName());
        assertNotEquals(newCategory, ingredientRepository.findById(id).get().getCategory());

        ingredientController.editIngredient(id, newFields);

        assertTrue(ingredientRepository.findById(id).isPresent());
        assertEquals(newName, ingredientRepository.findById(id).get().getName());
        assertEquals(newPrice, ingredientRepository.findById(id).get().getPrice());
        assertEquals(newAmount, ingredientRepository.findById(id).get().getAmount());
        assertEquals(newUnit.getName(), ingredientRepository.findById(id).get().getUnit().getName());
        assertEquals(newCategory, ingredientRepository.findById(id).get().getCategory());
    }



    @Test
    void ingredientChangeEveryFieldByRequest()throws Exception{
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

        assertTrue(ingredientRepository.findById(id).isPresent());
        assertNotEquals(newName, ingredientRepository.findById(id).get().getName());
        assertNotEquals(newPrice, ingredientRepository.findById(id).get().getPrice());
        assertNotEquals(newAmount, ingredientRepository.findById(id).get().getAmount());
        assertNotEquals(newUnit.getName(), ingredientRepository.findById(id).get().getUnit().getName());
        assertNotEquals(newCategory, ingredientRepository.findById(id).get().getCategory());



        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/ingredients/" + id)
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
