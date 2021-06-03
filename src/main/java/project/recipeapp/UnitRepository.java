package project.recipeapp;

import org.springframework.data.jpa.repository.JpaRepository;
import project.recipeapp.units.Unit;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String name);

}
