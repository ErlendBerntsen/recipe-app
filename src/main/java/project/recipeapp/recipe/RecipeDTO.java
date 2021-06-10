package project.recipeapp.recipe;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {
    private String name;
    private String description = "";
    private String steps = "";
    private String notes = "";
    private String glass = "";
    private double rating;
    private double difficulty;
    private List<RecipeIngredientDTO> ingredients;

    public RecipeDTO(){

    }

    public RecipeDTO(String name, String description, String steps, String notes, String glass, double rating, double difficulty, List<RecipeIngredientDTO> ingredients){
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.notes = notes;
        this.glass = glass;
        this.rating = rating;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
    }
}
