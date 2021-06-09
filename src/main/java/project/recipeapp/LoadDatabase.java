package project.recipeapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.recipeapp.ingredient.Ingredient;
import project.recipeapp.ingredient.IngredientRepository;
import project.recipeapp.units.volumes.CentiLiter;
import project.recipeapp.units.volumes.Liter;
import project.recipeapp.units.Unit;
import project.recipeapp.units.weights.Gram;


@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);


    @Bean
    CommandLineRunner initDatabase(IngredientRepository ingredientRepository, UnitRepository unitRepository){
        return args -> {
            Unit liter = new Liter();
            Unit centiliter = new CentiLiter();
            Unit gram = new Gram();
            unitRepository.save(liter);
            unitRepository.save(centiliter);
            unitRepository.save(gram);

            unitRepository.findAll().forEach(unit -> {
                log.info("Preloaded " + unit);
            });

            ingredientRepository.save(new Ingredient("Absolut Vodka", 464.90, 1, liter, Category.VODKA));
            ingredientRepository.save(new Ingredient("Captain Morgan White Rum", 325.90, 70, centiliter, Category.RUM));
            ingredientRepository.save(new Ingredient("Sugar", 22.90, 1000, gram, Category.MISC));

            ingredientRepository.findAll().forEach(ingredient -> {
                log.info("Preloaded " + ingredient);
            });
        };
    }


}
