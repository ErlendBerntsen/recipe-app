package project.recipeapp.recipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.recipeapp.UnitRepository;
import project.recipeapp.ingredient.IngredientRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UnitRepository unitRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeModelAssembler assembler;

    public RecipeController(RecipeRepository recipeRepository, UnitRepository unitRepository,
                            IngredientRepository ingredientRepository, RecipeModelAssembler assembler){
        this.recipeRepository = recipeRepository;
        this.unitRepository = unitRepository;
        this.ingredientRepository = ingredientRepository;
        this.assembler = assembler;
    }

    @PostMapping("/recipes")
    public ResponseEntity<?> newRecipe(@RequestBody RecipeDTO recipeDTO){
        var recipe = new Recipe();
        fromRecipeDTOtoRecipe(recipe, recipeDTO);
        recipeRepository.save(recipe);
        EntityModel<Recipe> entityModel = assembler.toModel(recipe);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/recipes")
    public CollectionModel<EntityModel<Recipe>> all(){
        List<EntityModel<Recipe>> recipes = new ArrayList<>();
        for(Recipe recipe : recipeRepository.findAll()){
            recipes.add(assembler.toModel(recipe));
        }
        return CollectionModel.of(recipes, linkTo(methodOn(RecipeController.class).all()).withSelfRel());
    }

    @GetMapping("/recipes/{id}")
    public EntityModel<Recipe> one(@PathVariable Long id) {
        var recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
        return  assembler.toModel(recipe);
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {

        recipeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/recipes/{id}")
    public ResponseEntity<?> editRecipe(@PathVariable Long id, @RequestBody JsonPatch patch)  {
        var recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode recipeJsonNode = objectMapper.convertValue(recipe,JsonNode.class);
        formatRecipeJsonNode(recipeJsonNode);
        try{
            JsonNode editedRecipe = patch.apply(recipeJsonNode);
            var recipeDTO = objectMapper.convertValue(editedRecipe, RecipeDTO.class);
            fromRecipeDTOtoRecipe(recipe, recipeDTO);
        }catch (JsonPatchException e){
            return ResponseEntity.badRequest().build();
        }
        recipe.calculatePrice();
        recipeRepository.save(recipe);
        return ResponseEntity.ok().body(assembler.toModel(recipe));
    }

    private void formatRecipeJsonNode(JsonNode recipeJsonNode){
        ((ObjectNode) recipeJsonNode).remove("id");
        ((ObjectNode) recipeJsonNode).remove("price");
        ArrayNode ingredients = (ArrayNode) recipeJsonNode.get("ingredients");
        for(int i = 0; i < ingredients.size(); i++){
            JsonNode ingredientName = ingredients.get(i).get("ingredient").get("name");
            ((ObjectNode)ingredients.get(i)).set("ingredient", ingredientName);

            JsonNode unitName = ingredients.get(i).get("unit").get("name");
            ((ObjectNode)ingredients.get(i)).set("unit", unitName);
        }
        ((ObjectNode) recipeJsonNode).put("ingredients", ingredients);
    }

    private void fromRecipeDTOtoRecipe(Recipe recipe, RecipeDTO recipeDTO) {
        recipe.setName(recipeDTO.getName());
        recipe.setPortions(recipeDTO.getPortions());
        recipe.setDescription(recipeDTO.getDescription());
        recipe.setSteps(recipeDTO.getSteps());
        recipe.setNotes(recipeDTO.getNotes());
        recipe.setGlass(recipeDTO.getGlass());
        recipe.setRating(recipeDTO.getRating());
        recipe.setDifficulty(recipeDTO.getDifficulty());
        List<RecipeIngredient> ingredients = new ArrayList<>();
        for(RecipeIngredientDTO recipeIngredientDTO : recipeDTO.getIngredients()){
            if(unitRepository.findByNameIgnoreCase(recipeIngredientDTO.getUnit()).isPresent()){
                if(ingredientRepository.findByNameIgnoreCase(recipeIngredientDTO.getIngredient()).isPresent()){
                    ingredients.add(new RecipeIngredient(
                            recipeIngredientDTO.getName(),
                            ingredientRepository.findByNameIgnoreCase(recipeIngredientDTO.getIngredient()).get(),
                            recipeIngredientDTO.getAmount(),
                            unitRepository.findByNameIgnoreCase(recipeIngredientDTO.getUnit()).get(),
                            recipeIngredientDTO.isGarnish()));
                }
            }
        }
        recipe.setIngredients(ingredients);
    }

}
