package project.recipeapp.recipe;

import lombok.Data;
import project.recipeapp.ingredient.Ingredient;
import project.recipeapp.units.Unit;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class RecipeIngredient {
    @ManyToOne
    Ingredient ingredient;

    double amount;

    @ManyToOne
    Unit unit;

    public RecipeIngredient(){

    }

    public RecipeIngredient(Ingredient ingredient, double amount, Unit unit){
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public String toString(){
        return "Name: " + ingredient.getName()
                + ", Amount: " + amount
                + ", Unit: " + unit.getAbbreviation();
    }
}
