package project.recipeapp.recipe;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {

    private String name;
    private int portions;
    private String description = "";
    private List<Step> steps;
    private String notes = "";
    private String glass = "";
    private double rating;
    private double difficulty;
    private List<RecipeIngredientDTO> ingredients;


    public RecipeDTO(){

    }

    public RecipeDTO(String name, int portions, String description, List<Step> steps, String notes, String glass, double rating, double difficulty, List<RecipeIngredientDTO> ingredients){
        this.name = name;
        this.portions = portions;
        this.description = description;
        this.steps = steps;
        this.notes = notes;
        this.glass = glass;
        this.rating = rating;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
    }
}
