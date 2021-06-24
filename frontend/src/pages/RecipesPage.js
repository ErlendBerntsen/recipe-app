import React from "react"
import {MasterPage} from "./MasterPage";
import useSWR from "swr";
import {getAllRecipes} from "../api/Methods";
import {Col, Container, Row, Spinner} from "react-bootstrap";
import {Recipe} from "../api/Classes";
import RecipeCard from "../components/RecipeCard";
import {Link} from "react-router-dom";
import {Fab} from "@material-ui/core";

export default function RecipesPage(){
    let recipeFetchDisplay = GetRecipes();
    const recipeButton = getButton();
    return(<MasterPage>
            <br/>
            {recipeButton}
            {recipeFetchDisplay}
        </MasterPage>

    );
}

function GetRecipes(){
    const{data, error} = useSWR('/api/recipes', getAllRecipes())
    if(error){
        return <p>Error in loading data</p>
    }else if(!data){
        return (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
    }else{
        return <RecipeList recipes={data._embedded.recipeList}> </RecipeList>;
    }
}

function RecipeList(props){
    const recipes = props.recipes.map(recipe => {
        const recipeObject = new Recipe(recipe);
        return (
            <Col style={{marginTop: "15px", marginBottom: "15px"}} xs={12} md={6} lg={4} xl={3}>
                <RecipeCard recipe={recipeObject}/>
            </Col>
        );
    });
    return (<Container> <Row>{recipes}</Row></Container>);
}

function getButton(){
    return(
        <Link as={Fab} to="/createrecipe">
            <Fab variant="extended" size="medium" color="primary">
                Create New Recipe
            </Fab>
        </Link>
    )
}