import React from "react";
import {Ingredient, IngredientDTO} from "../api/Classes";
import {IngredientCard} from "../components/IngredientCard";
import {Col, Container, Row, Spinner} from "react-bootstrap";
import {MasterPage} from "./MasterPage";
import {createNewIngredient, getAllCategories, getAllIngredients, getAllUnits} from "../api/Methods";
import useSWR from 'swr'
import IngredientModal from "../components/IngredientModal";

let units;
let categories;
export default function IngredientsPage() {

    let unitFetchDisplay = GetUnits();
    let categoryFetchDisplay;
    let ingredientsFetchDisplay;

    if (units !== null) {
        categoryFetchDisplay = GetCategories()
    }

    if(categories!== null){
        ingredientsFetchDisplay = GetIngredients()
    }

    function handleClick(){
        const ingredient = new IngredientDTO("Test drink", 123, 70, "cl", "MISC");
        createNewIngredient(JSON.stringify(ingredient))
            .then(response => {
                console.log(response);
            })
    }

    const testing = false;
    return(<MasterPage>
            <br/>
        {testing? <button onClick={() => handleClick()}>Create test ingredient</button> : <></>}
                    {units===null? unitFetchDisplay : categoryFetchDisplay}
                    <br/>
                    <br/>
                    {ingredientsFetchDisplay}
        </MasterPage>
    );
}




function GetUnits(){
    const {data, error} = useSWR('/api/units', getAllUnits())
    if(error) {
        return <p>Error in loading unit data</p>;
    }else if(!data){
        return <p>Loading units...</p>
    }else{
        units = data;
        return <p>Received units</p>
    }
}

function GetCategories(){
    const {data, error} = useSWR('/api/categories', getAllCategories())
    if(error){
        return <p>Error in loading category data</p>;
    }else if(!data){
        return <p>Loading categories...</p>
    }else{
        categories = data;
        return <IngredientModal ingredient={null} units={units} categories={categories} />
    }
}

function GetIngredients(){
    const {data, error} = useSWR('/api/ingredients', getAllIngredients())

    if(error){
        return <p>Error in loading ingredients</p>;
    }else if(!data){
        return  (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
    }else{
        return <IngredientList ingredients={data._embedded.ingredientList}/>;
    }
}

function IngredientList(props) {
    const ingredients = props.ingredients.map(ingredient => {
        const newIngredient = new Ingredient(ingredient);
        return (<Col style={{marginTop: "15px", marginBottom: "15px"}} xs={12} md={6} lg={4} xl={3}>
            <IngredientCard ingredient={newIngredient} units={units} categories={categories} />
        </Col>);
    });

    return <Container>{<Row>{ingredients}</Row>}</Container>;
}




