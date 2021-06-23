import React, {useState} from "react"
import {Card, Badge, Button, Col, Row, Modal} from "react-bootstrap"
import IngredientModal from "./IngredientModal";
import {deleteIngredient} from "../api/Methods";
import IconButton from '@material-ui/core/IconButton';
import {DeleteForever} from "@material-ui/icons";

export function IngredientCard(props){
    const [showDeleteWarning, setShowDeleteWarning] = useState(false);

    const handleCloseDeleteWarning = () => setShowDeleteWarning(false);
    const handleShowDeleteWarning = () => setShowDeleteWarning(true)

    const ingredient = props.ingredient;
    function handleDelete(){
        deleteIngredient(ingredient.id)
            .then(response => {
                response.ok? alert("Deleted successfully") : alert("Error in deletion");
                handleCloseDeleteWarning();
                window.location.reload();
            });
    }
    const space = "         ";
    return(
        <Card>
            <Card.Img variant="top" src={"./" + ingredient.id + ".jpg"} height="100" alt="Ingredient picture"/>
            <Card.Body style={{backgroundColor: "#ffffff", height: "160px"}}>
                <Card.Title >{ingredient.name}</Card.Title>
                <Card.Text>
                    Kr {ingredient.price}, {ingredient.amount} {ingredient.unit} {space}
                    <Badge pill variant="info">
                        {ingredient.category}
                    </Badge>
                </Card.Text>

                <Row>
                    <Col xs>
                        <IngredientModal edit={true} ingredient={ingredient} units={props.units} categories={props.categories} />
                    </Col>
                    <Col xs>
                        <>
                            <IconButton onClick={() => handleShowDeleteWarning()}>
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
            </Card.Body>
        </Card>
    );
}

