package project.recipeapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.recipeapp.units.Unit;
import project.recipeapp.units.volumes.CentiLiter;
import project.recipeapp.units.volumes.Liter;
import project.recipeapp.units.volumes.MilliLiter;


import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitConversionTest {
    private Unit milliLiter;
    private Unit centiLiter;
    private Unit liter;
    private double amount;

    @BeforeEach
    void setUp(){
        milliLiter = new MilliLiter();
        centiLiter = new CentiLiter();
        liter = new Liter();
        amount = 1234.56789;
    }

    @Test
    void centiLiterShouldBe10TimesMilliliter(){
        assertEquals(amount * 10,  Unit.convert(amount, milliLiter, centiLiter));
        assertEquals(amount / 10,  Unit.convert(amount, centiLiter, milliLiter));
    }

    @Test
    void literShouldBe1000TimesMilliliter(){
        assertEquals(amount * 1000,  Unit.convert(amount, milliLiter, liter));
        assertEquals(amount / 1000,  Unit.convert(amount, liter, milliLiter));
    }

    @Test
    void literShouldBe100TimesCentiliter(){
        assertEquals(amount * 100,  Unit.convert(amount, centiLiter, liter));
        assertEquals(amount / 100,  Unit.convert(amount, liter, centiLiter));
    }
}
