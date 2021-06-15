export class Ingredient{
    constructor(props) {
        this.id = props.id
        this.name = props.name;
        this.price = props.price;
        this.amount = props.amount;
        this.unit = props.unit.abbreviation;
        this.category = props.category;
        this.links = [props._links.self.href, props._links.ingredients.href];
    }
}

export default Ingredient