import React from "react";
import Ingredient from "./Classes";
import IngredientCard from "./IngredientCard";
import {CardDeck} from "react-bootstrap";
import {Link} from "react-router-dom";


export default function IngredientsPage() {

    const [data, upDateData] = React.useState(null);
    const [firstLoad, setLoad] = React.useState(true);
    let isLoading = true;

    async function sampleFunc() {
        let response = await fetch("/api/ingredients");
        let body = await response.json();
        upDateData(body);
    }

    if (firstLoad) {
        sampleFunc();
        setLoad(false);
    }

    if (data !== null) isLoading = false;


    return(
        <div>
            <Link to="/"> Go to homepage</Link>
            <br/>
            {isLoading? (<p>fetching data...</p>):
                (<IngredientList ingredients={data._embedded.ingredientList}/>)
            }
        </div>
    );

}

function IngredientList(props) {
    const ingredients = props.ingredients.map(ingredient => {
        const newIngredient = new Ingredient(ingredient);
        return <IngredientCard ingredient={newIngredient}/>
    });
    return <CardDeck>{ingredients}</CardDeck>
}

