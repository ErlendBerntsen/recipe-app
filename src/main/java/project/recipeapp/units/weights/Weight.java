package project.recipeapp.units.weights;

import project.recipeapp.units.Unit;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

@Entity
@Inheritance
public abstract class Weight extends Unit {

}
