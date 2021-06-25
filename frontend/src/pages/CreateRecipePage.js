import React from "react";
import {MasterPage} from "./MasterPage";
import RecipeForm from "../components/RecipeForm";

export default function CreateRecipePage(){
    return(
        <MasterPage>
            <RecipeForm> </RecipeForm>
        </MasterPage>
    );
}