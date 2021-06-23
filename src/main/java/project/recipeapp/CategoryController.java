package project.recipeapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CategoryController {

    public CategoryController(){

    }

    @GetMapping("/categories")
    @CrossOrigin
    public ResponseEntity<List<String>> all(){
        List<String> categories = Arrays.stream(Category.values())
                .map(Category::toString)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(categories);
    }
}
