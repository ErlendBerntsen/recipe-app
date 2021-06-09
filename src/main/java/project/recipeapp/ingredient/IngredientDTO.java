package project.recipeapp.ingredient;

import lombok.Data;
import project.recipeapp.Category;

@Data
public class IngredientDTO {


    private String name;
    private double price;
    private double amount;
    private String unit;
    private Category category;

    public IngredientDTO(String name, double price, double amount, String unit, Category category){
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }
}
