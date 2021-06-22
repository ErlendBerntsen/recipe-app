import React from "react";
import {Ingredient, IngredientDTO} from "../api/Classes";
import {IngredientCard} from "../components/IngredientCard";
import { Col, Container, Row} from "react-bootstrap";
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

                    <button onClick={() => handleClick()}>Create test ingredient</button>
                    {unitsFetchDisplay}
                    <br/>
                    <br/>
                    {ingredientsFetchDisplay}



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
        return (<Col style={{marginTop: "15px", marginBottom: "15px"}} xs={12} md={6} lg={4} xl={3}>
            <IngredientCard units={units} ingredient={newIngredient}/>
        </Col>);
    });

    return <Container>{<Row>{ingredients}</Row>}</Container>;
}




