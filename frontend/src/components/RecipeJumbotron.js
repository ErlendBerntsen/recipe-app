import React from "react";
import { Image, Jumbotron, ListGroup, ListGroupItem, Navbar} from "react-bootstrap";

export default function RecipeJumbotron(props){
    const recipe = props.recipe;
    const ingredients = recipe.ingredients;
    const lists = getIngredients(ingredients);
    const priceText = recipe.price + "kr";
    const ratingText = recipe.rating + "/10";
    const difficultyText = recipe.difficulty + "/10";
    console.log(ingredients)
    return(
        <Jumbotron>
            <text>{JSON.stringify(recipe)}</text>
            <Image style={{  display: "block", marginLeft: "auto", marginRight: "auto"}} rounded alt="Recipe image" src="../recipes/12.jpg"/>
            <br/>
            <h1>{recipe.name}</h1>
            <h5>{recipe.description}</h5>
            <br/>
            <h6>Price {priceText}</h6>
            <h6>Rating {ratingText}</h6>
            <h6>Difficulty  {difficultyText}</h6>
            <br/>
            <Navbar bg="light">
                <h3>Ingredients</h3>
            </Navbar>
            <ListGroup variant="flush">
                {lists.ingredientList}
            </ListGroup>
            <Navbar bg="light">
                <h5>Garnish</h5>
            </Navbar>
            <ListGroup variant="flush">
                {lists.garnishList}
            </ListGroup>
            <br/>
            <br/>
            <Navbar bg="light">
                <h3>Steps</h3>
            </Navbar>
            <ListGroup >
               <ListGroupItem>{recipe.steps}</ListGroupItem>
            </ListGroup>
        </Jumbotron>
    );
}

function getIngredients(ingredients){
    let ingredientList = [];
    let garnishList = []
    ingredients.forEach(ingredient => {
        let info = ingredient.amount  + ingredient.unit.abbreviation + " " + ingredient.name;
        if(!ingredient.garnish){
            info += " (" + ingredient.ingredient.name + ")"
            ingredientList.push(<ListGroup.Item>{info}</ListGroup.Item>);
        }else{
            garnishList.push(<ListGroup.Item>{info}</ListGroup.Item>)
        }
    });
    return {ingredientList: ingredientList, garnishList: garnishList};
}