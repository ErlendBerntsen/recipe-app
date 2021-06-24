package project.recipeapp.recipe;

import lombok.Data;

import javax.persistence.Embeddable;


@Data
@Embeddable
public class Step {
    private String step;

    public Step(){

    }

    public Step(String step){
        this.step = step;
    }
}
