import { useParams } from "react-router-dom";
import React from "react";
import {MasterPage} from "./MasterPage";
import {Spinner} from "react-bootstrap";
import useSWR from "swr";
import {getRecipe} from "../api/Methods";
import {Recipe} from "../api/Classes";
import RecipeJumbotron from "../components/RecipeJumbotron";

export default function RecipePage(){
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

        return <RecipeJumbotron recipe={new Recipe(data)}/>
    }
}