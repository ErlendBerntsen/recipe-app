package project.recipeapp.recipe;

import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int portions = 1;
    private String description = "";
    private String steps = "";
    private String notes = "";
    private String glass = "";
    private double rating;
    private double difficulty;


    @ElementCollection(fetch =  FetchType.EAGER)
    private List<RecipeIngredient> ingredients;


    public Recipe (){

    }

    public Recipe(String name,  List<RecipeIngredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    @Override
    public String toString(){
        return "Name: " + name + "\n"
                + "Portions: " + portions + "\n"
                + "Description: " + description + "\n"
                + "Steps:\n" + steps + "\n"
                + "Notes: " + notes + "\n"
                + "Glass: " + glass + "\n"
                + "Rating: " + rating + "/10\n"
                + "Difficulty: " + difficulty + "/10\n"
                + "Ingredients: " + ingredients.toString();
    }


}
