import React from "react";
import {Ingredient, IngredientDTO} from "../api/Classes";
import IngredientCard from "../components/IngredientCard";
import {CardDeck} from "react-bootstrap";
import {MasterPage} from "./MasterPage";
import {createNewIngredient, getAllIngredients, getAllUnits} from "../api/Methods";
import useSWR from 'swr'
import IngredientModal from "../components/IngredientModal";

let units;

export default function IngredientsPage() {
    let unitsFetchDisplay = GetUnits()
    let ingredientsFetchDisplay;
    if(units !== null){
        ingredientsFetchDisplay = GetIngredients()
    }

    function handleClick(){
        const ingredient = new IngredientDTO("Test drink", 123, 70, "cl", "MISC");
        createNewIngredient(JSON.stringify(ingredient))
            .then(response => {
                console.log(response);

            })
    }

    return(<MasterPage>
            <br/>
                <>
                    <button onClick={() => handleClick()}>Create test ingredient</button>
                    {unitsFetchDisplay}
                    <br/>
                    <br/>
                    {ingredientsFetchDisplay}

                </>

        </MasterPage>
    );
}

function GetUnits(){
    const {data, error} = useSWR('/api/units', getAllUnits())

    if(error){
        return <p>Error in loading units</p>;
    }else if(!data){
        return  <p>Loading...</p>;
    }else{
        units = data;
        return <IngredientModal units={data} ingredient={null}/>
    }
}

function GetIngredients(){
    const {data, error} = useSWR('/api/ingredients', getAllIngredients())

    if(error){
        return <p>Error in loading ingredients</p>;
    }else if(!data){
        return  <p>Loading...</p>;
    }else{
        return <IngredientList ingredients={data._embedded.ingredientList}/>;
    }
}

function IngredientList(props) {
    const ingredients = props.ingredients.map(ingredient => {
        const newIngredient = new Ingredient(ingredient);
        return <IngredientCard units={units} ingredient={newIngredient}/>
    });
    let list = [];
    let start = 0;
    while(start < ingredients.length){
        let end = Math.min(start + 5, ingredients.length)
        list.push(<><CardDeck>{ingredients.slice(start, end)}</CardDeck> <br/></>);
        start += 5;
    }
    return list;
}




