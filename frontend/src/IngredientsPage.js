import React from 'react';
import axios from "axios";
import useSWR from "swr";
import Ingredient from "./Classes";
import IngredientCard from "./IngredientCard";
import {CardDeck} from "react-bootstrap"



class IngredientsPage extends React.Component{
    render(){
        return <IngredientsData />
    }
}

function IngredientsData(){
    const backend = axios.create({
        baseURL: "http://localhost:8080",
    });
    const fetch = url => backend.get("/ingredients").then(response => response.data)
    const {data, error} = useSWR('/',  fetch)
    if(error) return <div>error</div>
    if(!data) return <div>loading...</div>
    return <IngredientList ingredients={data._embedded.ingredientList}></IngredientList>
}

function IngredientList(props) {
    const ingredients = props.ingredients.map(ingredient => {
        const newIngredient = new Ingredient(ingredient);
        return <IngredientCard ingredient={newIngredient}></IngredientCard>
    });
    return <CardDeck>{ingredients}</CardDeck>
}

export default IngredientsPage;
