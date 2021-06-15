import React from "react"
import {Card, Badge} from "react-bootstrap"

class IngredientCard extends React.Component{
    render(){
        const ingredient = this.props.ingredient;
        return(
            <Card style={{ width: '16rem' }}>
                <Card.Img variant="top" src="./5.jpg" alt="Ingredient image"/>
                <Card.Body>
                    <Card.Title>{ingredient.name}</Card.Title>
                    <Card.Text>
                        Kr {ingredient.price}, {ingredient.amount} {ingredient.unit}
                        <br/>
                        <Badge pill variant="info">
                                {ingredient.category}
                        </Badge>
                    </Card.Text>
                </Card.Body>
            </Card>
        );
    }
}

export default IngredientCard