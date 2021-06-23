import axios from 'axios'

export function getAllIngredients(){
    return(
    axios.get('/api/ingredients')
            .then(response => {
                return response;
            })
            .catch(error => {
                return error;
            })
    );
}




export function createNewIngredient(ingredient){
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: ingredient
    };
    return fetch('/api/ingredients/', requestOptions);

}

export function deleteIngredient(ingredientId){
    const requestOptions = {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
    };
    return fetch('/api/ingredients/' + ingredientId, requestOptions);
}

export function editIngredient(ingredientId, patch){
    const requestOptions = {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: patch
    };
    return fetch('/api/ingredients/' + ingredientId, requestOptions);
}

export function getAllUnits(){
    return(
        axios.get('/api/units')
            .then(response => {
                return response;
            })
            .catch(error => {
                return error;
            })
    );
}

export function getAllCategories(){
    return(
        axios.get('/api/categories')
            .then(response => {
                return response;
            })
            .catch(error => {
                return error;
            })
    );
}


