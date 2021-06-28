package project.recipeapp;


public enum Category {
    GIN("Gin"),
    VODKA("Vodka"),
    WHISKEY("Whiskey"),
    RUM("Rum"),
    BRANDY("Brandy"),
    TEQUILA("Tequila"),
    MEZCAL("Mezcal"),
    LIQUEUR("Liqueur"),
    ORANGE_LIQUEUR("Orange Liqueur"),
    AMARO("Amaro"),
    WINE("Wine"),
    JUICE("Juice"),
    SOFT_DRINK("Soft Drink"),
    SYRUP("Syrup"),
    BITTERS("Bitters"),
    FRIDGE_PANTRY("Fridge/Pantry"),
    BEER("Beer"),
    MISC("Miscellaneous");


    private final String categoryName;
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
