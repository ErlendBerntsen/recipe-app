import React from "react";
import {Card, Col, Row} from "react-bootstrap";
import {Link} from "react-router-dom";

export default function RecipeCard(props){
    const name = props.recipe.name;
    return (
        <Card>
            <Card.Img variant="top" src={"../recipes/12.jpg"} height="100" alt="Ingredient picture"/>
            <Card.Header>
                <Card.Link as={Link} to={"/recipes/" + props.recipe.id}>
                    <Card.Title>{name}</Card.Title>
                </Card.Link>
            </Card.Header>
        </Card>
    );
}