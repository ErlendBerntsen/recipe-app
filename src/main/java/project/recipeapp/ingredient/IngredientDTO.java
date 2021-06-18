package project.recipeapp.ingredient;

import lombok.Data;

@Data
public class IngredientDTO {


    private String name;
    private double price;
    private double amount;
    private String unit;
    private String category;

    public IngredientDTO(){

    }

    public IngredientDTO(String name, double price, double amount, String unit, String category){
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }
}
