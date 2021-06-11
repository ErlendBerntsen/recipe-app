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
    private double ratioToMainUnit;

    public static double convert(double amount, Unit fromUnit, Unit toUnit) {
        double amountInMainUnit = fromUnit.convertToMainUnit(amount);
        return toUnit.convertFromMainUnit(amountInMainUnit);
    }

    public double convertFromMainUnit(double amount){
        return amount / this.getRatioToMainUnit();
    }

    public double convertToMainUnit(double amount){
        return amount * this.getRatioToMainUnit();
    }

    public abstract String getName();

    public abstract String getAbbreviation();

    public abstract double getRatioToMainUnit();


}


