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


    const allUnits = props.units.map(unit => {
        return <option>{unit.abbreviation}</option>
    })
    const allCategories = props.categories.map(category => {
        return <option>{category}</option>
    })

    const chooseUnit = "Choose unit";
    const chooseCategory = "Choose category";

    const validations = [name !== '', price >= 0 && price !== null, amount >= 1 && amount !== null
        , unit !== '' && unit !== chooseUnit, category !== '' && category !== chooseCategory]


    function handleClick(){
        isCreating? handleCreation() : handleEdit()
    }

    function handleCreation(){
        createNewIngredient('/api/ingredients', JSON.stringify(new IngredientDTO(name, price, amount, unit, category)))
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
        editIngredient('/api/ingredients/', props.ingredient.id, patch)
            .then(handleResponse);
    }


    function handleResponse(response){
        let successText = isCreating? "created a new" : "edited";
        let errorText = isCreating? "creating" : "editing"
        response.ok? setAlert(<Alert variant="success">Successfully {successText} ingredient</Alert>) :
                setAlert(<Alert variant="danger">Error in {errorText} ingredient</Alert>);
        window.location.reload();
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
                    <Form.Control as="select" defaultValue={isCreating? chooseUnit : unit}
                                  onChange={(event) => setUnit(event.target.value)}
                                  isValid={validations[3]}>
                        <option>{chooseUnit}</option>
                        {allUnits}
                    </Form.Control>
                </Form.Group>
            </Form.Row>

            <Form.Group controlId="formCategory">
                <Form.Label>Category</Form.Label>
                <Form.Control as="select" defaultValue={isCreating? chooseCategory : category}
                              onChange={(event) => setCategory(event.target.value)}
                              isValid={validations[4]}>>
                    <option>{chooseCategory}</option>
                    {allCategories}
                </Form.Control>
            </Form.Group>
            <Button  disabled={!validations.every(Boolean)} type="submit" variant="primary" onClick={() => handleClick()}>
                {isCreating ? "Create Ingredient" : "Save Changes"}
            </Button>

        </Form>

    );
    return (alert === null? form : alert);

}
