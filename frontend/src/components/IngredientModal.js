import React, {useState} from "react";
import { Button, Modal} from "react-bootstrap";
import IngredientForm from "./IngredientForm";

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

    return (
        <>
            <Button variant={color} size={size} onClick={handleShow}>
                {text}
            </Button>
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

