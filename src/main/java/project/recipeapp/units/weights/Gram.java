package project.recipeapp.units.weights;

import javax.persistence.Entity;

@Entity
public class Gram extends Weight {

    private String name;
    private String abbreviation;

    public Gram() {
        name = "Gram";
        abbreviation = "g";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }
}
