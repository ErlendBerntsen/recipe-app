package project.recipeapp.units.volumes;

import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;

@Entity
public class CentiLiter extends Volume {

    private String name;
    private String abbreviation;
    private double ratioToMainUnit;

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
