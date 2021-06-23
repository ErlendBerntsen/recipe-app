import axios from 'axios'

export function getAllIngredients(url){
    return(
    axios.get(url)
            .then(response => {
                return response;
            })
            .catch(error => {
                return error;
            })
    );
}

export function createNewIngredient(url, ingredient){
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: ingredient
    };
    return fetch(url, requestOptions);

}

export function deleteIngredient(url, ingredientId){
    const requestOptions = {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
    };
    return fetch(url + ingredientId, requestOptions);
}

export function editIngredient(url, ingredientId, patch){
    const requestOptions = {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: patch
    };
    return fetch(url + ingredientId, requestOptions);
}

export function getAllUnits(url){
    return(
        axios.get(url)
            .then(response => {
                return response;
            })
            .catch(error => {
                return error;
            })
    );
}

export function getAllCategories(url){
    return(
        axios.get(url)
            .then(response => {
                return response;
            })
            .catch(error => {
                return error;
            })
    );
}

export function getAllRecipes(url){
    return(
        axios.get(url)
            .then(response => {
                return response;
            })
            .catch(error => {
                return error;
            })
    );
}

export function getRecipe(url){
    return(
        axios.get(url)
            .then(response => {
                return response;
            })
            .catch(error => {
                return error;
            })
    );
}

