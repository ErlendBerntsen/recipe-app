package project.recipeapp.units;


import javax.persistence.Entity;

@Entity
public  class Piece extends Unit {

    private final String name;
    private final String abbreviation;
    private final double ratioToMainUnit;

    public Piece(){
        name = "Piece";
        abbreviation = "pcs";
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