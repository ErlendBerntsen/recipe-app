import React from "react";
import {MasterPage} from "./MasterPage";
import RecipeForm from "../components/RecipeForm";

export default function CreateRecipePage(){
    return(
        <MasterPage>
            <h1>Recipe creation page</h1>
            <RecipeForm> </RecipeForm>
        </MasterPage>
    );
}