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
import project.recipeapp.ingredient.Ingredient;
import project.recipeapp.ingredient.IngredientRepository;
import project.recipeapp.recipe.*;
import project.recipeapp.units.Unit;
import project.recipeapp.units.volumes.CentiLiter;
import project.recipeapp.units.volumes.Liter;
import project.recipeapp.units.volumes.MilliLiter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipeControllerTest {

    @Autowired
    private RecipeController recipeController;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private MockMvc mockMvc;
    String baseURI;

    private RecipeModelAssembler assembler;
    private RecipeDTO recipeDTO;
    private String recipeName;
    private int portions;
    private String description;
    private String steps;
    private String glass;
    private Double rating;
    private Double difficulty;
    private String notes;
    private List<RecipeIngredientDTO> ingredients;
    private Ingredient gin;
    private Ingredient lemonJuice;
    private Ingredient raspberrySyrup;
    private Ingredient eggWhite;
    private RecipeIngredientDTO ginDTO;
    private RecipeIngredientDTO lemonJuiceDTO;
    private RecipeIngredientDTO raspberrySyrupDTO;
    private RecipeIngredientDTO eggWhiteDTO;
    private Unit milliLiter;
    private Unit centiLiter;
    private Unit liter;

    @BeforeEach
    void setUp(){
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
        unitRepository.deleteAll();


        recipeName = "Clover Club";
        steps = "\t1. Combine all the ingredients into your cocktail"+
                "\t2. Dry shake (without ice)"+
                "\t3. Wet shake (with ice)"+
                "\t4. Strain into a chilled coupe"+
                "\t5. Garnish with skewered raspberries";
        description = "A sour and sweet cocktail from the early 1900's with taste of lemon and raspberries.";
        glass = "Coupe";
        rating = 7.0;
        difficulty = 6.0;
        notes = "Too much gin taste, and needs a little less egg whites";


        milliLiter = new MilliLiter();
        centiLiter = new CentiLiter();
        liter = new Liter();

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
        ginDTO = new RecipeIngredientDTO("Gin","Beefeater London Dry", 45, "milliLiter", false);
        lemonJuiceDTO = new RecipeIngredientDTO ("Lemon Juice", "Lemon Juice", 22.5, "milliLiter", false);
        raspberrySyrupDTO  = new RecipeIngredientDTO ("Raspberry Syrup", "Raspberry Syrup", 22.5,"milliLiter", false);
        eggWhiteDTO = new RecipeIngredientDTO ("Egg White", "Egg White", 15, "milliLiter", false);
        ingredients.add(ginDTO);
        ingredients.add(lemonJuiceDTO);
        ingredients.add(raspberrySyrupDTO);
        ingredients.add(eggWhiteDTO);

        recipeDTO = new RecipeDTO(recipeName, 1, description, steps, notes, glass, rating, difficulty, ingredients);
        assembler = new RecipeModelAssembler();
        recipeController = new RecipeController(recipeRepository, unitRepository, ingredientRepository, assembler);
        baseURI = "/api/recipes/";
    }

    @Test
    void recipeShouldBeCreatedWithController(){
        assertEquals(0, recipeRepository.count());
        recipeController.newRecipe(recipeDTO);
        var recipeFound = recipeRepository.findByNameIgnoreCase(recipeName);
        assertTrue(recipeFound.isPresent());
        var recipe = recipeFound.get();
        assertEquals(recipeName, recipe.getName());
        for(int i = 0; i < ingredients.size(); i++){
            assertEquals(ingredients.get(i).getName(), recipe.getIngredients().get(i).getName());
        }
    }

    @Test
    void twoRecipesShouldBeCreatedWithController(){
        assertEquals(0, recipeRepository.count());
        recipeController.newRecipe(new RecipeDTO("Recipe 1",1, "", "", "", "", 0.0, 0.0, new ArrayList<>()));
        recipeController.newRecipe(new RecipeDTO("Recipe 2",1, "", "", "", "", 0.0, 0.0, new ArrayList<>()));
        assertEquals(2, recipeRepository.count());
    }

    @Test
    void recipeShouldBeCreatedWithURIRequest() throws Exception{
        String jsonRecipe = toJson(recipeName, ingredients);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseURI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRecipe);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is (recipeName)))
                .andExpect(jsonPath("$.ingredients[0].name", is (ginDTO.getName())))
                .andExpect(jsonPath("$.ingredients[1].name", is (lemonJuiceDTO.getName())))
                .andExpect(jsonPath("$.ingredients[2].name", is (raspberrySyrupDTO.getName())))
                .andExpect(jsonPath("$.ingredients[3].name", is (eggWhiteDTO.getName())));
    }

    private String toJson(String recipeName, List<RecipeIngredientDTO> ingredients){
        StringBuilder jsonRecipe = new StringBuilder();
        jsonRecipe.append("{\"name\":\"").append(recipeName).append("\"").append(",\"ingredients\":[");
        for(RecipeIngredientDTO ingredientDTO : ingredients){
            jsonRecipe.append("{\"name\":\"").append(ingredientDTO.getName()).append("\"")
                    .append(",\"ingredient\":\"").append(ingredientDTO.getIngredient()).append("\"")
                    .append(",\"amount\":").append(ingredientDTO.getAmount())
                    .append(",\"unit\":\"").append(ingredientDTO.getUnit()).append("\"},");
        }

        if(!ingredients.isEmpty()){
            jsonRecipe.delete(jsonRecipe.length()-1, jsonRecipe.length()); //Delete last comma of list
        }
        jsonRecipe.append("]}");
        return jsonRecipe.toString();
    }


    @Test
    void allRecipesShouldBeReturnedWithController(){
        assertEquals(recipeRepository.count(), recipeController.all().getContent().size());
        recipeController.newRecipe(recipeDTO);
        assertEquals(recipeRepository.count(), recipeController.all().getContent().size());
    }

    @Test
    void allRecipesShouldBeReturnedWithURIRequest()throws Exception{
        assertEquals(recipeRepository.count(), recipeController.all().getContent().size());
        recipeController.newRecipe(recipeDTO);
        this.mockMvc.perform(get(baseURI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.recipeList", hasSize((int) recipeRepository.count())));
    }

    @Test
    void oneRecipeShouldBeReturnedWithController(){
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);
        assertEquals(recipe.getId(), recipeController.one(recipe.getId()).getContent().getId());
    }

    @Test
    void oneRecipeShouldBeReturnedWithURIRequest() throws Exception{
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);
        this.mockMvc.perform(get(baseURI + recipe.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$.id", is (recipe.getId().intValue())));
    }

    @Test
    void recipeShouldContainSelfLink()throws Exception{
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);
        String selfLink = "http://localhost" + baseURI + recipe.getId();
        this.mockMvc.perform(get(baseURI + recipe.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$._links.self.href", is (selfLink)));
    }

    @Test
    void recipeShouldContainAggregateRootLink()throws Exception{
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);
        String aggregateRootLink = "http://localhost" + baseURI;
        aggregateRootLink = aggregateRootLink.substring(0, aggregateRootLink.length()-1);
        this.mockMvc.perform(get(baseURI + recipe.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$._links.recipes.href", is (aggregateRootLink)));
    }

    @Test
    void listOfAllRecipesShouldContainSelfLink()throws Exception{
        String selfLink = "http://localhost" + baseURI;
        selfLink = selfLink.substring(0, selfLink.length()-1);
        this.mockMvc.perform(get(baseURI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$._links.self.href", is (selfLink)));
    }

    @Test
    void everyRecipeInListOfAllRecipesShouldContainSelfAndAggregateRootLink()throws Exception{
        Recipe recipe1 = new Recipe("Recipe1", new ArrayList<>());
        Recipe recipe2 = new Recipe("Recipe2", new ArrayList<>());
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);

        String path = "$._embedded.recipeList[";
        String aggregateRootLink = "http://localhost" + baseURI;
        aggregateRootLink = aggregateRootLink.substring(0, aggregateRootLink.length()-1);

        this.mockMvc.perform(get(baseURI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(path +   "0]._links.self.href",
                        is(aggregateRootLink + "/" + recipe1.getId())))
                .andExpect(jsonPath(path + "0]._links.recipes.href",
                        is(aggregateRootLink)))
                .andExpect(jsonPath(path +   "1]._links.self.href",
                        is(aggregateRootLink + "/" + recipe2.getId())))
                .andExpect(jsonPath(path  + "1]._links.recipes.href",
                        is(aggregateRootLink)));
    }

    @Test
    void creatingRecipeResponseShouldContainLinks()throws Exception{
        String jsonRecipe = toJson(recipeName, new ArrayList<>());

        String aggregateRootLink = "http://localhost" + baseURI;
        aggregateRootLink = aggregateRootLink.substring(0, aggregateRootLink.length()-1);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(baseURI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRecipe);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath( "$._links.self.href",
                        is (aggregateRootLink + "/" + recipeRepository.findByNameIgnoreCase(recipeName).get().getId())))
                .andExpect(jsonPath( "$._links.recipes.href", is (aggregateRootLink)));
    }

    @Test
    void recipeNotFoundShouldHaveAppropriateErrorMessageWithController(){
        Long badId = -1L;
        String errorMessage = "Could not find recipe " + badId;
        try{
            recipeController.one(badId);
        }catch (RecipeNotFoundException e){
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    void recipeNotFoundShouldHaveAppropriateErrorMessageWithURIRequest()throws Exception{
        long badId = -1L;
        String errorMessage = "Could not find recipe " + badId;
        this.mockMvc.perform(get(baseURI + badId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(errorMessage)));
    }

    @Test
    void recipeShouldBeDeletedWithController(){
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);
        assertTrue(recipeRepository.findById(recipe.getId()).isPresent());
        recipeController.deleteRecipe(recipe.getId());
        assertFalse(recipeRepository.findById(recipe.getId()).isPresent());

    }

    @Test
    void recipeShouldBeDeletedWithURIRequest()throws Exception{
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);
        assertTrue(recipeRepository.findById(recipe.getId()).isPresent());
        this.mockMvc.perform(delete(baseURI + recipe.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        assertFalse(recipeRepository.findById(recipe.getId()).isPresent());
    }


    @Test
    void allBasicRecipeFieldsShouldBeEditedWithJsonPatch()throws Exception{
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);
        String newName = "new name";
        int newPortions = 2;
        String newDescription = "new description";
        String newSteps = "new steps";
        String newNotes = "new notes";
        String newGlass = "new glass";
        Double newRating = 10.0;
        Double newDifficulty = 10.0;
        String patch = "["
                + "{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"" + newName + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/portions\",\"value\":\"" + newPortions + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/description\",\"value\":\"" + newDescription + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/steps\",\"value\":\"" + newSteps + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/notes\",\"value\": \"" + newNotes + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/glass\",\"value\":\"" + newGlass + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/rating\",\"value\":\"" + newRating + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/difficulty\",\"value\":\"" + newDifficulty + "\"}]";

        MockHttpServletRequestBuilder builder =
                patch(baseURI + recipe.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(patch);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newName)))
                .andExpect(jsonPath("$.portions", is(newPortions)))
                .andExpect(jsonPath("$.description", is(newDescription)))
                .andExpect(jsonPath("$.steps", is(newSteps)))
                .andExpect(jsonPath("$.notes", is(newNotes)))
                .andExpect(jsonPath("$.glass", is(newGlass)))
                .andExpect(jsonPath("$.rating", is(newRating)))
                .andExpect(jsonPath("$.difficulty", is(newDifficulty)));
    }

    @Test
    void allRecipeIngredientFieldsShouldBeEditedWithJsonPatch()throws Exception{
        recipeController.newRecipe(recipeDTO);
        long id = recipeRepository.findByNameIgnoreCase(recipeDTO.getName()).get().getId();
        Ingredient vodka = new Ingredient("Absolut Vodka", 464.90, 1, liter, Category.VODKA);
        ingredientRepository.save(vodka);
        String newName = "newname";
        String newIngredient = vodka.getName();
        double newAmount = 100;
        String newUnit = centiLiter.getName();
        boolean isGarnish = true;
        String patch = "["
                + "{\"op\":\"replace\",\"path\":\"/ingredients/0/name\",\"value\":\"" + newName + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/ingredients/0/ingredient\",\"value\":\"" + newIngredient + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/ingredients/0/amount\",\"value\":\"" + newAmount + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/ingredients/0/unit\",\"value\":\"" + newUnit + "\"},"
                + "{\"op\":\"replace\",\"path\":\"/ingredients/0/garnish\",\"value\":\"" + isGarnish + "\"}"
                + "]";

        MockHttpServletRequestBuilder builder =
                patch(baseURI + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(patch);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients[0].name", is(newName)))
                .andExpect(jsonPath("$.ingredients[0].ingredient.name", is(newIngredient)))
                .andExpect(jsonPath("$.ingredients[0].amount", is(newAmount)))
                .andExpect(jsonPath("$.ingredients[0].unit.name", is(newUnit)))
                .andExpect(jsonPath("$.ingredients[0].garnish", is(isGarnish)));
    }

    @Test
    void ingredientShouldBeAddedToRecipe() throws Exception{
        recipeController.newRecipe(recipeDTO);
        long id = recipeRepository.findByNameIgnoreCase(recipeDTO.getName()).get().getId();
        Ingredient vodka = new Ingredient("Absolut Vodka", 464.90, 1, liter, Category.VODKA);
        ingredientRepository.save(vodka);
        String newIngredient = "{\"name\":\"Vodka\""
                + ",\"ingredient\":\"" + vodka.getName() + "\""
                + ",\"amount\":" + 60
                + ",\"unit\":\"" + "Milliliter" + "\""
                + ",\"garnish\":" + false + "}";
        String patch = "[{\"op\":\"add\",\"path\":\"/ingredients/-\",\"value\":" + newIngredient + "}]";


        MockHttpServletRequestBuilder builder =
                patch(baseURI + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(patch);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients[4].ingredient.name", is(vodka.getName())));
    }

    @Test
    void ingredientShouldBeRemovedFromRecipe() throws Exception{
        recipeController.newRecipe(recipeDTO);
        long id = recipeRepository.findByNameIgnoreCase(recipeDTO.getName()).get().getId();
        String patch = "[{\"op\":\"remove\",\"path\":\"/ingredients/0\"}]";


        MockHttpServletRequestBuilder builder =
                patch(baseURI + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(patch);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients", hasSize(3)))
                .andExpect(jsonPath("$.ingredients[0].ingredient.name", is(lemonJuice.getName())));
    }

    @Test
    void ingredientPriceShouldBeUpdated() throws Exception{
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);


        Ingredient vodka = new Ingredient("Absolut Vodka", 464.90, 1, liter, Category.VODKA);
        ingredientRepository.save(vodka);
        String newIngredient = "{\"name\":\"Vodka\""
                + ",\"ingredient\":\"" + vodka.getName() + "\""
                + ",\"amount\":" + vodka.getAmount()
                + ",\"unit\":\"" + vodka.getUnit().getName() + "\""
                + ",\"garnish\":" + false + "}";
        String patch = "[{\"op\":\"add\",\"path\":\"/ingredients/-\",\"value\":" + newIngredient + "}]";
        double expectedPrice = Math.round(vodka.getPrice());

        MockHttpServletRequestBuilder builder =
                patch(baseURI + recipe.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(patch);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(expectedPrice)));
    }

    @Test
    void editingRecipeResponseShouldContainLinks()throws Exception{
        Recipe recipe = new Recipe("", new ArrayList<>());
        recipeRepository.save(recipe);
        String newName = "new name";
        String patch = "[{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"" + newName + "\"}]";
        String aggregateRootLink = "http://localhost" + baseURI;
        aggregateRootLink = aggregateRootLink.substring(0, aggregateRootLink.length()-1);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(baseURI + recipe.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(patch);

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath( "$._links.self.href",
                        is (aggregateRootLink + "/" + recipe.getId())))
                .andExpect(jsonPath( "$._links.recipes.href", is (aggregateRootLink)));
    }




}
