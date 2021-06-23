import React from "react";
import {Card} from "react-bootstrap";
import {Link} from "react-router-dom";

export default function RecipeCard(props){
    const name = props.recipe.name;
    const description = props.recipe.description;
    const rating = props.recipe.rating;
    const price = props.recipe.price;
    return (
        <Card>
            <Card.Img variant="top" src={"../recipes/12.jpg"} height="100" alt="Ingredient picture"/>
            <Card.Header>
                <Card.Link as={Link} to={"/recipes/" + props.recipe.id}>
                    <Card.Title>{name}</Card.Title>
                </Card.Link>
            </Card.Header>
            <Card.Body>
                <Card.Text>{description}</Card.Text>
                <Card.Text>Price: {price}kr  </Card.Text>
                <Card.Text>Rating: {rating}/10</Card.Text>
            </Card.Body>
        </Card>
    );
}