import React, {useState} from "react";
import Ingredient from "../api/Classes";
import IngredientCard from "../components/IngredientCard";
import {Button, CardDeck, Col, Form, Modal} from "react-bootstrap";
import {MasterPage} from "./MasterPage";
import {getAllIngredients} from "../api/Methods";
import useSWR from 'swr'


export default function IngredientsPage() {

    const {data, error} = useSWR('/api/ingredients', getAllIngredients())

    const [unitData, updateUnitData] = React.useState(null);
    const [firstLoad, setLoad] = React.useState(true);
    let isLoading = true;

    async function sampleFunc() {
        let unitResponse = await  fetch("/api/units");
        let unitBody = await unitResponse.json();
        updateUnitData(unitBody);
    }

    if (firstLoad) {
        sampleFunc();
        setLoad(false);
    }

    if (unitData !== null) isLoading = false;
    let ingredientsFetchDisplay;
    let unitsFetchDisplay;

    if(error){
        ingredientsFetchDisplay = <p>Error in loading ingredients</p>;
    }else if(!data){
        ingredientsFetchDisplay = <p>Loading...</p>;
    }else if(data && !isLoading){
        ingredientsFetchDisplay = <IngredientList units={unitData} ingredients={data._embedded.ingredientList}/>;
        unitsFetchDisplay = <IngredientModal units={unitData} ingredient={null}/>;
    }
    return(<MasterPage>
            <br/>
            {
                <>
                    {isLoading? "fetching units" : "got units"}
                    {unitsFetchDisplay}
                    <br/>
                    <br/>
                    {ingredientsFetchDisplay}
                </>
            }
        </MasterPage>
    );

}



function IngredientList(props) {
    const ingredients = props.ingredients.map(ingredient => {
        const newIngredient = new Ingredient(ingredient);
        return <IngredientCard units={props.units}ingredient={newIngredient}/>
    });
    return (
        <CardDeck>
    {ingredients}
        </CardDeck>
    );
}

export function IngredientModal(props){
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const text = props.edit? "Edit Ingredient" : "Create A New Ingredient"
    const color = props.edit? "outline-danger" : "primary"
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

class IngredientForm extends React.Component{
    constructor(props) {
        super(props);
        this.state ={
            name: (props.ingredient === null? '' : props.ingredient.name),
            price: (props.ingredient === null? null : props.ingredient.price),
            amount: (props.ingredient === null? null : props.ingredient.amount),
            unit: (props.ingredient === null? '' : props.ingredient.unit),
            category: (props.ingredient === null? '' : props.ingredient.category),
        };
    }
    handleClick(){
        this.props.ingredient === null? this.createIngredient() : this.editIngredient()
        fetch('http://localhost:5000/api/ingredients')
        //TODO FIX THIS
        window.location.reload()

    }

    createIngredient(){
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(this.state)
        };
        fetch('http://localhost:5000/api/ingredients', requestOptions)
    }

    editIngredient(){
        let patch = '['
        const stateNames = ['name', 'price', 'amount', 'unit', 'category']
        const stateValues = [this.state.name, this.state.price, this.state.amount, this.state.unit, this.state.category]
        Object.keys(stateNames).map(i => {
            patch += '{"op": "replace", "path": "/' + stateNames[i] + '", "value": "' + stateValues[i] + '"'
            patch += (i == stateNames.length-1 ? '}]':'},')
        });
        console.log(patch)
        const requestOptions = {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: patch
        };
        fetch('http://localhost:5000/api/ingredients/' + this.props.ingredient.id, requestOptions)
    }


    render(){
        return (
            <Form>
                <Form.Group controlId="formName">
                    <Form.Label>Name</Form.Label>
                    <Form.Control placeholder ="Enter name" defaultValue={this.state.name} onChange={(event) => this.setState({name: event.target.value})}/>
                </Form.Group>

                <Form.Group controlId="formPrice">
                    <Form.Label>Price</Form.Label>
                    <Form.Control placeholder="Enter price"
                                  defaultValue={this.state.price}
                                  onChange={(event) => this.setState({price: parseFloat(event.target.value)})}/>
                </Form.Group>

                <Form.Row>
                    <Form.Group as={Col}controlId="formAmount">
                        <Form.Label>Amount</Form.Label>
                        <Form.Control  placeholder="Enter amount"
                                       defaultValue={this.state.amount}
                                       onChange={(event) => this.setState({amount: parseFloat(event.target.value)})}/>
                    </Form.Group>

                    <Form.Group as={Col} controlId="formUnit">
                        <Form.Label>Unit</Form.Label>
                        <Form.Control  as="select" defaultValue={this.state.unit} onChange={(event) => this.setState({unit: event.target.value})}>
                            {this.props.units}
                        </Form.Control>
                    </Form.Group>

                </Form.Row>

                <Form.Group controlId="formCategory">
                    <Form.Label>Category</Form.Label>
                    <Form.Control as="select" defaultValue="Choose category" onChange={(event) => this.setState({category: event.target.value})}>
                        <option>Choose category</option>
                        <option>RUM</option>
                        <option>VODKA</option>
                        <option>MISC</option>
                    </Form.Control>
                </Form.Group>
                <Button variant="primary"  onClick={() => this.handleClick()}>
                    {this.props.ingredient === null? "Create Ingredient" : "Save Changes"}
                </Button>

            </Form>

        );

    }
}


