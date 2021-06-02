package project.recipeapp.units.volumes;

import javax.persistence.Entity;

@Entity
public class Liter extends Volume {

    private String name;
    private String abbreviation;

    public Liter(){
        name = "Liter";
        abbreviation = "L";
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