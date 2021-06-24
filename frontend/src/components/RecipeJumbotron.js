import React from "react";
import {Col, Image, Jumbotron, ListGroup, ListGroupItem, Navbar, ProgressBar, Row} from "react-bootstrap";

export default function RecipeJumbotron(props){
    const recipe = props.recipe;
    const ingredients = recipe.ingredients;
    const lists = getIngredients(ingredients);
    const priceText = recipe.price + "kr";
    const difficultyText = recipe.difficulty + "/10";
    const ratingText = recipe.rating + "/10";

    const ratingColor = getColor(recipe.rating);
    const difficultyColor = getColor(10 - recipe.difficulty);

    function getColor(number){
        if(number >= 7) return "success";
        else if(number > 3) return "warning";
        else return "danger";
    }
    const ratingBar = <ProgressBar  now={recipe.rating * 10} variant={ratingColor} />
    const difficultyBar = <ProgressBar  now={recipe.difficulty * 10} variant={difficultyColor} />

    return(
        <Jumbotron>
            <text>{JSON.stringify(recipe)}</text>
            <Image style={{  display: "block", marginLeft: "auto", marginRight: "auto"}} rounded alt="Recipe image" src="../recipes/12.jpg"/>
            <br/>
            <h1>{recipe.name}</h1>
            <h5>{recipe.description}</h5>
            <br/>
            <ListGroup>
                <ListGroupItem>
                    <Row>
                        <Col>
                            <p>Portions</p>
                            <h3>{recipe.portions}</h3>
                        </Col>
                        <Col>
                            <p>Price</p>
                            <h3>{priceText}</h3>
                        </Col>
                        <Col>
                            <p>Rating</p>
                            <h3>{ratingText}</h3>
                            {ratingBar}
                        </Col>
                        <Col>
                            <p>Difficulty</p>
                            <h3>{difficultyText}</h3>
                            {difficultyBar}
                        </Col>
                    </Row>
                </ListGroupItem>
            </ListGroup>

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
                {getSteps(recipe.steps)}
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

function getSteps(steps){
    const stepsList = steps.split('|');
    let list = [];
    stepsList.forEach((step, index) => {
        const text = (index + 1) + ". " + step
        list.push(<ListGroupItem>{text}</ListGroupItem>)
    })
    return list;
}