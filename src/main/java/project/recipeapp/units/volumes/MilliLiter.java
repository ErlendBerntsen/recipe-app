package project.recipeapp.units.volumes;


import javax.persistence.Entity;

@Entity
public class MilliLiter extends Volume {

    private String name;
    private String abbreviation;
    private double ratioToMainUnit;

    public MilliLiter(){
        name = "Milliliter";
        abbreviation = "ml";
        ratioToMainUnit = 1000;
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
