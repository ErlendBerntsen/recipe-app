package project.recipeapp.units;


import javax.persistence.*;

@Entity
@Inheritance
public abstract class Unit {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String abbreviation;

    public abstract String getName();

    public abstract String getAbbreviation();


}


