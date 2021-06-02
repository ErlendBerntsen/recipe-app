package project.recipeapp;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class IngredientModelAssembler implements RepresentationModelAssembler<Ingredient, EntityModel<Ingredient>> {

    @Override
    public EntityModel<Ingredient> toModel(Ingredient ingredient){
        return EntityModel.of(ingredient,
                linkTo(methodOn(IngredientController.class).one(ingredient.getId())).withSelfRel(),
                linkTo(methodOn(IngredientController.class).all()).withRel("ingredients"));
    }
}
