package project.recipeapp.recipe;

import lombok.Data;
import project.recipeapp.ingredient.Ingredient;
import project.recipeapp.units.Unit;


import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class RecipeIngredient {

    private String name;

    @ManyToOne
    private Ingredient ingredient;

    private double amount;

    @ManyToOne
    private Unit unit;

    private boolean isGarnish;

    public RecipeIngredient(){

    }

    public RecipeIngredient(String name,  Ingredient ingredient, double amount, Unit unit, boolean isGarnish){
        this.name = name;
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
        this.isGarnish = isGarnish;
    }

    @Override
    public String toString(){
        return "Name: " + name
                + ", Ingredient: " + ingredient.getName()
                + ", Amount: " + amount
                + ", Unit: " + unit
                + ", Garnish: " + isGarnish;
    }
}
