package project.recipeapp.units.volumes;

import javax.persistence.Entity;

@Entity
public class CentiLiter extends Volume {

    private String name;
    private String abbreviation;

    public CentiLiter(){
        name = "Centiliter";
        abbreviation = "cl";
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
