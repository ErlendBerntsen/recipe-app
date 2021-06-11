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
                            unitRepository.findByNameIgnoreCase(recipeIngredientDTO.getUnit()).get()));
                }else{
                    return new ResponseEntity<>("Could not create new recipe. No such ingredient exists", HttpStatus.BAD_REQUEST);
                }

            }else{
                return new ResponseEntity<>("Could not create new recipe. No such unit exists", HttpStatus.BAD_REQUEST);
            }
        }
        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);
        return ResponseEntity.ok().body(recipe);
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
}
