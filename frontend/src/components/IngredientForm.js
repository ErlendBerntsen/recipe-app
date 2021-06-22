import React, {useState} from "react";
import {Alert, Button, Col, Form} from "react-bootstrap";
import {createNewIngredient, editIngredient} from "../api/Methods";
import {IngredientDTO} from "../api/Classes";

export default function IngredientForm(props){
    const isCreating = props.ingredient === null
    const [name, setName] = useState(isCreating? '' : props.ingredient.name)
    const [price, setPrice] = useState(isCreating? null : props.ingredient.price)
    const [amount, setAmount] = useState(isCreating? null : props.ingredient.amount)
    const [unit, setUnit] = useState(isCreating? '' : props.ingredient.unit)
    const [category, setCategory] = useState(isCreating? '' : props.ingredient.category)
    const [alert, setAlert] = useState(null);

    const validations = [name !== '', price >= 0 && price !== null, amount >= 1 && amount !== null, unit !== '', category !== '' && category !== 'Choose category']
    function handleClick(){
        isCreating? handleCreation() : handleEdit()
    }

    function handleCreation(){
        createNewIngredient(JSON.stringify(new IngredientDTO(name, price, amount, unit, category)))
            .then(handleResponse);
    }

    function handleEdit() {
        let patch = '['
        const stateNames = ['name', 'price', 'amount', 'unit', 'category']
        const stateValues = [name, price, amount, unit, category]
        Object.keys(stateNames).map(i => {
            patch += '{"op": "replace", "path": "/' + stateNames[i] + '", "value": "' + stateValues[i] + '"'
            patch += (i == stateNames.length - 1 ? '}]' : '},')
        });
        editIngredient(props.ingredient.id, patch)
            .then(handleResponse);
    }


    function handleResponse(response){
        let successText = isCreating? "created a new" : "edited";
        let errorText = isCreating? "creating" : "editing"
        response.ok? setAlert(<Alert variant="success">Successfully {successText} ingredient</Alert>) :
                setAlert(<Alert variant="danger">Error in {errorText} ingredient</Alert>);
    }

    let form = (
        <Form>
            <Form.Group controlId="formName">
                <Form.Label>Name</Form.Label>
                <Form.Control type="text" placeholder="Enter name" defaultValue={name}
                              onChange={(event) => setName(event.target.value)}
                              isValid={validations[0]}/>
            </Form.Group>

            <Form.Group controlId="formPrice">
                <Form.Label>Price</Form.Label>
                <Form.Control type="number" placeholder="Enter price"
                              defaultValue={price}
                              onChange={(event) => setPrice(parseFloat(event.target.value))}
                              isValid={validations[1]}
                              isInvalid={price < 0}/>
            </Form.Group>

            <Form.Row>
                <Form.Group as={Col} controlId="formAmount">
                    <Form.Label>Amount</Form.Label>
                    <Form.Control type="number" placeholder="Enter amount"
                                  defaultValue={amount}
                                  onChange={(event) => setAmount(parseFloat(event.target.value))}
                                  isValid={validations[2]}
                                  isInvalid={amount !== null && amount <= 0}/>
                </Form.Group>

                <Form.Group as={Col} controlId="formUnit">
                    <Form.Label>Unit</Form.Label>
                    <Form.Control as="select" defaultValue={unit}
                                  onChange={(event) => setUnit(event.target.value)}
                                  isValid={validations[3]}>
                        {props.units}
                    </Form.Control>
                </Form.Group>
            </Form.Row>

            {//TODO ADD CATEGORY FETCH
            }
            <Form.Group controlId="formCategory">
                <Form.Label>Category</Form.Label>
                <Form.Control as="select" defaultValue={isCreating? "Choose category" : category}
                              onChange={(event) => setCategory(event.target.value)}
                              isValid={validations[4]}>>
                    <option>Choose category</option>
                    <option>RUM</option>
                    <option>VODKA</option>
                    <option>MISC</option>
                </Form.Control>
            </Form.Group>
            <Button  disabled={!validations.every(Boolean)} type="submit" variant="primary" onClick={() => handleClick()}>
                {isCreating ? "Create Ingredient" : "Save Changes"}
            </Button>

        </Form>

    );
    return (alert === null? form : alert);

}
