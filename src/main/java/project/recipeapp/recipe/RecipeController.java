package project.recipeapp.recipe;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.recipeapp.UnitRepository;
import project.recipeapp.ingredient.Ingredient;
import project.recipeapp.ingredient.IngredientModelAssembler;
import project.recipeapp.ingredient.IngredientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RecipeController {

    private RecipeRepository recipeRepository;
    private UnitRepository unitRepository;
    private IngredientRepository ingredientRepository;
    private RecipeModelAssembler assembler;

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
                }else{
                    return new ResponseEntity<>("Could not create new recipe. No such ingredient exists", HttpStatus.BAD_REQUEST);
                }

            }else{
                return new ResponseEntity<>("Could not create new recipe. No such unit exists", HttpStatus.BAD_REQUEST);
            }
        }
        recipe.setIngredients(ingredients);
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
    public ResponseEntity<?> editRecipe(@PathVariable Long id, @RequestBody Map<String, Object> newFields) {
        var recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
        for(String field : newFields.keySet()){
            switch (field){
                case "name" :
                    recipe.setName((String) newFields.get(field));
                    break;
                case "portions" :
                    recipe.setPortions((int) newFields.get(field));
                    break;
                case "description" :
                    recipe.setDescription((String) newFields.get(field));
                    break;
                case "steps" :
                    recipe.setSteps((String) newFields.get(field));
                    break;
                case "notes" :
                    recipe.setNotes((String) newFields.get(field));
                    break;
                case "glass" :
                    recipe.setGlass((String) newFields.get(field));
                    break;
                case "rating" :
                    recipe.setRating((double) newFields.get(field));
                    break;
                case "difficulty" :
                    recipe.setDifficulty((double) newFields.get(field));
                    break;
                case "ingredients" :
                    List<Map<String, Object>> ingredients = (List<Map<String, Object>>) newFields.get(field);
                    for(int i = 0; i < ingredients.size(); i++){
                        Map<String, Object> recipeIngredient = ingredients.get(i);
                        for(String recipeIngredientField : recipeIngredient.keySet()){
                            switch (recipeIngredientField) {
                                case "name" :
                                    recipe.getIngredients().get(i).setName((String) recipeIngredient.get(recipeIngredientField));
                                    break;
                            }

                        }
                    }


                    break;
            }
        }
        recipeRepository.save(recipe);
        return ResponseEntity.ok().body(recipe);
    }

}
