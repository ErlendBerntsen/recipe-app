package project.recipeapp;

import org.springframework.data.jpa.repository.JpaRepository;
import project.recipeapp.units.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}
