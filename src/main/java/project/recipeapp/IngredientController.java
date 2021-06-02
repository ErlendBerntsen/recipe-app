package project.recipeapp;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class IngredientController {
    private final IngredientRepository repository;
    private final IngredientModelAssembler assembler;

    IngredientController(IngredientRepository repository, IngredientModelAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/ingredients")
    public CollectionModel<EntityModel<Ingredient>> all(){
        List<EntityModel<Ingredient>> ingredients = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ingredients, linkTo(methodOn(IngredientController.class).all()).withSelfRel());
    }

    @PostMapping("/ingredients")
    public ResponseEntity<?> newIngredient(@RequestBody Ingredient ingredient){
        EntityModel<Ingredient> entityModel = assembler.toModel(repository.save(ingredient));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/ingredients/{id}")
    public EntityModel<Ingredient> one(@PathVariable Long id){
        var ingredient = repository.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
        return assembler.toModel(ingredient);
    }


    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/ingredients/{id}")
    public ResponseEntity<?> editIngredient(@PathVariable Long id, @RequestBody Map<String, Object> newFields) {
        var ingredient = repository.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
        newFields.forEach((key, value) -> {
            var field = ReflectionUtils.findField(Ingredient.class, key);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, ingredient, value);
            field.setAccessible(false);

        });

        EntityModel<Ingredient> entityModel = assembler.toModel(repository.save(ingredient));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
