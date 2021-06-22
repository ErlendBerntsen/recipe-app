import React, {useState} from "react"
import {Card, Badge, Button, Col, Row, Modal} from "react-bootstrap"
import IngredientModal from "./IngredientModal";
import {deleteIngredient} from "../api/Methods";
import IconButton from '@material-ui/core/IconButton';
import {DeleteForever} from "@material-ui/icons";

function IngredientCard(props){
    const [showDeleteWarning, setShowDeleteWarning] = useState(false);

    const handleCloseDeleteWarning = () => setShowDeleteWarning(false);
    const handleShowDeleteWarning = () => setShowDeleteWarning(true)

    const ingredient = props.ingredient;
    function handleDelete(){

        deleteIngredient(ingredient.id)
            .then(response => {
                response.ok? alert("Deleted successfully") : alert("Error in deletion");
            }).then(handleCloseDeleteWarning)
    }
    return(
        <Card border="secondary" style={{ width: '12rem' }}>
            <Card.Img variant="top" src={"./" + ingredient.id + ".jpg"} height="100" alt="Ingredient image"/>
            <Card.Header  >
                <Card.Title >{ingredient.name}</Card.Title>
            </Card.Header>
            <Card.Body>
                <Card.Text>
                    Kr {ingredient.price}, {ingredient.amount} {ingredient.unit}
                    <br/>
                    <Badge pill variant="info">
                        {ingredient.category}
                    </Badge>
                </Card.Text>
            </Card.Body>
            <Card.Footer>
                <Row>
                    <Col xs>
                        <IngredientModal edit={true} units={props.units} ingredient={ingredient}/>
                    </Col>
                    <Col xs>
                        <>
                            <IconButton onClick={handleShowDeleteWarning}>
                                <DeleteForever />
                            </IconButton>
                            <Modal show={showDeleteWarning} onHide={handleCloseDeleteWarning}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Warning</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>Are you sure you want to delete this ingredient? This action can't be undone.</Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={handleCloseDeleteWarning}>
                                        Close
                                    </Button>
                                    <Button variant="danger" onClick={handleDelete}>
                                        Delete ingredient
                                    </Button>
                                </Modal.Footer>
                            </Modal>
                        </>
                    </Col>
                </Row>
            </Card.Footer>
        </Card>
    );
}

export default IngredientCard