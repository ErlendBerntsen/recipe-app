import React, {useState} from "react";
import { Button, Modal} from "react-bootstrap";
import IngredientForm from "./IngredientForm";
import IconButton from "@material-ui/core/IconButton";
import {Edit} from "@material-ui/icons";

export default function IngredientModal(props){
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const text = props.edit? "Edit" : "Create A New Ingredient"
    const color = props.edit? "outline-warning" : "primary"
    const size = props.edit? "sm" : "lg"
    const units = props.units.map(unit => {
        return <option>{unit.abbreviation}</option>
    })

    const button = props.edit? <IconButton onClick={handleShow}><Edit/></IconButton>
        : <Button variant={color} size={size} onClick={handleShow}>{text}</Button>

    return (
        <>
            {button}
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{text}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <IngredientForm ingredient={props.ingredient} units={units} handleClose={handleClose}> </IngredientForm>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

