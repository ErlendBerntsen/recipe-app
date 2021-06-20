import React from "react";
import axios from 'axios'

export function getAllIngredients(){
    return(
    axios.get('/api/ingredients')
            .then(response => {
                return response
                console.log(response.data);
            })
            .catch(error => {
                console.log(error);
            })
    );
}

