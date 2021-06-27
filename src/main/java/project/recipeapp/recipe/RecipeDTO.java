package project.recipeapp.recipe;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {

    private String name;
    private int portions;
    private String description = "";
    private String steps = "";
    private String notes = "";
    private String glass = "";
    private Double rating;
    private Double difficulty;
    private List<RecipeIngredientDTO> ingredients;


    public RecipeDTO(){

    }

    public RecipeDTO(String name, int portions, String description, String steps, String notes, String glass, Double rating, Double difficulty, List<RecipeIngredientDTO> ingredients){
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
