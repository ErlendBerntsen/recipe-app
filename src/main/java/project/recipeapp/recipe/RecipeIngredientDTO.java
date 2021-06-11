package project.recipeapp.recipe;

import lombok.Data;


@Data
public class RecipeIngredientDTO {

    private String name;

    private String ingredient;

    private double amount;

    private String unit;

    private boolean isGarnish;

    public RecipeIngredientDTO(){

    }

    public RecipeIngredientDTO(String name, String ingredient, double amount, String unit, boolean isGarnish){
        this.name = name;
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
        this.isGarnish = isGarnish;
    }


}
