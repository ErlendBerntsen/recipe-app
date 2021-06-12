package project.recipeapp.ingredient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.recipeapp.*;



import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class IngredientController {
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final IngredientModelAssembler assembler;

    public IngredientController(IngredientRepository ingredientRepository, UnitRepository unitRepository, IngredientModelAssembler assembler){
        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
        this.assembler = assembler;
    }

    @GetMapping("/ingredients")
    public CollectionModel<EntityModel<Ingredient>> all(){
        List<EntityModel<Ingredient>> ingredients = ingredientRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ingredients, linkTo(methodOn(IngredientController.class).all()).withSelfRel());
    }

    @PostMapping("/ingredients")
    public ResponseEntity<?> newIngredient(@RequestBody IngredientDTO ingredientDTO){
        if(unitRepository.findByNameIgnoreCase(ingredientDTO.getUnit()).isPresent()){
            var ingredient = new Ingredient();
            fromIngredientDTOtoIngredient(ingredient,ingredientDTO);
            EntityModel<Ingredient> entityModel = assembler.toModel(ingredient);
            return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        }
        else{
            return new ResponseEntity<>("Could not create new ingredient. No such unit exists", HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary =  "Get an ingredient by its id")
    @GetMapping("/ingredients/{id}")
    public EntityModel<Ingredient> one(
            @Parameter (description = "id of ingredient to be searched") @PathVariable Long id)
    {
        var ingredient = ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
        return assembler.toModel(ingredient);
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Long id) {
        ingredientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/ingredients/{id}")
    public ResponseEntity<?> editIngredient(@PathVariable Long id, @RequestBody JsonPatch patch) throws Exception {
        var ingredient = ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode ingredientJsonNode = objectMapper.convertValue(ingredient, JsonNode.class);
        ((ObjectNode)ingredientJsonNode).remove("id");
        ((ObjectNode)ingredientJsonNode).put("unit", ingredientJsonNode.get("unit").get("name"));
        ingredientJsonNode = patch.apply(ingredientJsonNode);
        IngredientDTO ingredientDTO = objectMapper.convertValue(ingredientJsonNode, IngredientDTO.class);
        fromIngredientDTOtoIngredient(ingredient, ingredientDTO);
        return ResponseEntity.ok().body(assembler.toModel(ingredient));
    }

    private void fromIngredientDTOtoIngredient(Ingredient ingredient, IngredientDTO ingredientDTO){
        ingredient.setName(ingredientDTO.getName());
        ingredient.setPrice(ingredientDTO.getPrice());
        ingredient.setAmount(ingredientDTO.getAmount());
        ingredient.setUnit(unitRepository.findByNameIgnoreCase(ingredientDTO.getUnit()).get());
        ingredient.setCategory(Category.getCategory(ingredientDTO.getCategory()));
        ingredientRepository.save(ingredient);
    }

}
