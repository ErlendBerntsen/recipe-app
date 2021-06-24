export class Ingredient{
    constructor(props) {
        this.id = props.id;
        this.name = props.name;
        this.price = props.price;
        this.amount = props.amount;
        this.unit = props.unit.abbreviation;
        this.category = props.category;
        this.links = [props._links.self.href, props._links.ingredients.href];
    }
}

export class IngredientDTO{
    constructor(name, price, amount, unit, category) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }
}

export class Recipe{
    constructor(props) {
        this.id = props.id;
        this.name = props.name;
        this.portions = props.portions;
        this.description = props.description;
        this.steps = props.steps;
        this.notes = props.notes;
        this.glass = props.glass
        this.rating = props.rating;
        this.difficulty = props.difficulty;
        this.price = props.price;
        this.ingredients = props.ingredients;
        this.links = [props._links.self.href, props._links.recipes.href];
    }
}

