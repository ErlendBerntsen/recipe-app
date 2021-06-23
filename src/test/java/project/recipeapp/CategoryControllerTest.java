package project.recipeapp;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryControllerTest {

    CategoryController controller = new CategoryController();

    @Test
    void controllerShouldReturnAllCategories(){
        List<String> categories = controller.all().getBody();
        assertNotNull(categories);
        for(Category category : Category.values()){
            assertTrue(categories.contains(category.toString()));
        }
    }
}
