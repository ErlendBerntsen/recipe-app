package project.recipeapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.recipeapp.units.Unit;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class IngredientController {
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final IngredientModelAssembler assembler;

    IngredientController(IngredientRepository ingredientRepository, UnitRepository unitRepository, IngredientModelAssembler assembler){
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
    public ResponseEntity<?> newIngredient(@RequestBody Ingredient ingredient){
        EntityModel<Ingredient> entityModel = assembler.toModel(ingredientRepository.save(ingredient));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/ingredients/{id}")
    public EntityModel<Ingredient> one(@PathVariable Long id){
        var ingredient = ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
        return assembler.toModel(ingredient);
    }


    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Long id) {
        ingredientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/ingredients/{id}")
    public ResponseEntity<?> editIngredient(@PathVariable Long id, @RequestBody Map<String, Object> newFields) {

        var ingredient = ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
        updateFields(newFields, ingredient);

        EntityModel<Ingredient> entityModel = assembler.toModel(ingredientRepository.save(ingredient));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    /*
    This is an extremely brittle solution. It works as intended i think, but will most certainly break with changes to ingredient
     */
    private void updateFields (Map<String, Object> newFields, Ingredient ingredient){
        for(String field : newFields.keySet()){
            switch (field){
                case "name":
                    ingredient.setName((String) newFields.get(field));
                    break;
                case "price":
                    ingredient.setPrice((double) newFields.get(field));
                    break;
                case "amount":
                    ingredient.setAmount((double) newFields.get(field));
                    break;
                case "unit":
                    HashMap<String, String> map = (HashMap) newFields.get(field);
                    var unit = unitRepository.findByName(map.get("name"));
                    if(unit.isPresent()){
                        ingredient.setUnit(unit.get());
                    }
                    break;
                case "category":
                    ingredient.setCategory(Category.valueOf((String) newFields.get(field)));
            }

        }
    }



}
