package project.recipeapp;


public enum Category {
    VODKA("Vodka"),
    RUM("Rum"),
    MISC("Miscellaneous"),
    ORANGE_LIQUEUR("Orange Liqueur");

    private String categoryName;
    Category(String categoryName){
        this.categoryName = categoryName;
    }

    @Override
    public String toString(){
        return categoryName;
    }


}
