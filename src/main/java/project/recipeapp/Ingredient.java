package project.recipeapp;

import lombok.Data;
import project.recipeapp.units.Unit;

import javax.persistence.*;

@Entity
@Data
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private double price;
    private double amount;

    @ManyToOne
    private Unit unit;

    @Enumerated(EnumType.STRING)
    private Category category;

    public Ingredient(){
    }

    public Ingredient(String name, double price,  double amount, Unit unit, Category category){
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    @Override
    public String toString(){
        return "Name = " + name
        + ", Price = " + price + " kr"
        + ", Quantity = " +  amount + unit.getAbbreviation()
        + ", Category = " +  category.toString();
    }
}
