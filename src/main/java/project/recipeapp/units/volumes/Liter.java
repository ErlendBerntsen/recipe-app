package project.recipeapp.units.volumes;

import javax.persistence.Entity;

@Entity
public  class Liter extends Volume {

    private String name;
    private String abbreviation;
    private double ratioToMainUnit;

    public Liter(){
        name = "Liter";
        abbreviation = "L";
        ratioToMainUnit = 1;
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