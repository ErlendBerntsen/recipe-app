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

    String name;
    String description = "";
    String steps = "";
    String notes = "";
    String glass = "";
    double rating;
    double difficulty;

    @ElementCollection
    List<RecipeIngredient> ingredients;


    public Recipe (){

    }

    public Recipe(String name,  List<RecipeIngredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    @Override
    public String toString(){
        return "Name: " + name + "\n"
                + "Description: " + description + "\n"
                + "Steps:\n" + steps + "\n"
                + "Notes: " + notes + "\n"
                + "Glass: " + glass + "\n"
                + "Rating: " + rating + "/10\n"
                + "Difficulty: " + difficulty + "/10\n"
                + "Ingredients: " + ingredients.toString();
    }


}
