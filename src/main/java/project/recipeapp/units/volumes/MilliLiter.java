package project.recipeapp.units.volumes;


import javax.persistence.Entity;

@Entity
public class MilliLiter extends Volume {

    private String name;
    private String abbreviation;

    public MilliLiter(){
        name = "Milliliter";
        abbreviation = "ml";
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
