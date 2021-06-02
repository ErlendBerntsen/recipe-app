package project.recipeapp;


public enum Category {
    VODKA("Vodka"),
    RUM("Rum"),
    MISC("Miscellaneous");

    private String categoryName;
    Category(String categoryName){
        this.categoryName = categoryName;
    }

    @Override
    public String toString(){
        return categoryName;
    }


}
