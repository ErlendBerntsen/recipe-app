import React from "react"
import {Card, Badge, Button, Col, Row} from "react-bootstrap"
import IngredientModal from "./IngredientModal";
import {deleteIngredient} from "../api/Methods";


class IngredientCard extends React.Component{
    render(){
        const ingredient = this.props.ingredient;
        function handleDelete(){
            deleteIngredient(ingredient.id)
                .then(response => {
                    response.ok? alert("Deleted successfully") : alert("Error in deletion");
                })
        }
        return(
            <Card style={{ width: '4rem' }}>
                <Card.Img variant="top" src={"./" + ingredient.id + ".jpg"}  width="auto" height="100" alt="Ingredient image"/>
                <Card.Body>
                    <Card.Title>{ingredient.name}</Card.Title>
                    <Card.Text>
                        Kr {ingredient.price}, {ingredient.amount} {ingredient.unit}
                        <br/>
                        <Badge pill variant="info">
                                {ingredient.category}
                        </Badge>
                    </Card.Text>
                    <Row>
                        <Col xs>
                            <IngredientModal edit={true} units={this.props.units} ingredient={ingredient}/>
                        </Col>
                        <Col xs>
                            <Button size="sm" variant="outline-danger" onClick={() => handleDelete()}>Delete</Button>
                        </Col>
                    </Row>

                </Card.Body>
            </Card>
        );
    }
}

export default IngredientCard