package project.recipeapp.units.volumes;

import javax.persistence.Entity;

@Entity
public class CentiLiter extends Volume {

    private final String name;
    private final String abbreviation;
    private final double ratioToMainUnit;

    public CentiLiter(){
        name = "Centiliter";
        abbreviation = "cl";
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
