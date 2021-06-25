import React from "react";
import {MasterPage} from "./MasterPage";
import RecipeForm from "../components/RecipeForm";
import {useParams} from "react-router-dom";
import useSWR from "swr";
import {getRecipe} from "../api/Methods";
import {Spinner} from "react-bootstrap";
import {Recipe} from "../api/Classes";

export default function EditRecipePage(){
    let recipeFetchDisplay = GetRecipe();
    return(
        <MasterPage>
            <br/>
            {recipeFetchDisplay}
        </MasterPage>
    );
}

function GetRecipe(){
    const { id } = useParams();
    const {data, error} = useSWR("/api/recipes/" + id, getRecipe())
    if(error){
        return <p>Error loading data</p>
    }else if(!data){
        return (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
    }else{
        return <RecipeForm recipe={new Recipe(data)}/>
    }
}