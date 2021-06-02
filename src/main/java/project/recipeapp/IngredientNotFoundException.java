package project.recipeapp;

public class IngredientNotFoundException extends RuntimeException{
    IngredientNotFoundException(Long id){
        super("Could not find ingredient " + id);
    }
}