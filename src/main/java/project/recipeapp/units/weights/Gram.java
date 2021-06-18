package project.recipeapp.units.weights;

import javax.persistence.Entity;

@Entity
public class Gram extends Weight {

    private final String name;
    private final String abbreviation;
    private final double ratioToMainUnit;

    public Gram() {
        name = "Gram";
        abbreviation = "g";
        ratioToMainUnit = 100;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public double getRatioToMainUnit (){
        return ratioToMainUnit;
    }
}
