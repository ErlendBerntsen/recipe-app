package project.recipeapp;


public enum Category {
    VODKA("Vodka"),
    RUM("Rum"),
    MISC("Miscellaneous"),
    ORANGE_LIQUEUR("Orange Liqueur"),
    GIN("Gin"),
    JUICE("Juice"),
    FRIDGE_PANTRY("Fridge/Pantry");

    private String categoryName;
    Category(String categoryName){
        this.categoryName = categoryName;
    }

    @Override
    public String toString(){
        return categoryName;
    }

    public static Category getCategory(String categoryName){
        for(Category category : Category.values()){
            if(category.toString().equals(categoryName)) return category;
        }
        return Category.valueOf(categoryName);
    }

}
