import React, {useState} from "react"
import {Card, Col, DropdownButton, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import {Add, DeleteOutline, Remove} from "@material-ui/icons";
import {Fab, Switch} from "@material-ui/core";
import DropdownItem from "react-bootstrap/DropdownItem";
import IconButton from "@material-ui/core/IconButton";

export default function BarTabCard(props){
    const barTab = props.barTab;
    const [paid, setPaid] = useState(props.paid);
    const [recipes, setRecipes] = useState(barTab.recipes);
    const allRecipes = props.allRecipes;

    const handleAmountUpdate = (index, amountAdded) => {
        console.log(JSON.stringify(allRecipes));

        let recipe =  recipes[index];
        if(recipe.amount === 0 && amountAdded === -1)return;
        recipe.amount = recipe.amount + amountAdded;
        setRecipes([...recipes.slice(0, index), recipe, ...recipes.slice(index +1)]);
    }

    const handleNewRecipe = (recipe) => {
        const newRecipe = {
            name: recipe.name,
            price: recipe.price,
            amount: 1,
        }
        setRecipes(prevRecipes => [...prevRecipes, newRecipe]);
    }

    const handleRemoveRecipe = (index) => {
        setRecipes([...recipes.slice(0, index), ...recipes.slice(index +1)]);
    }

    function getRecipes(recipeList){
        let list = []
        recipeList.forEach((recipe, index)=> {
            list.push(
                <ListGroup.Item>
                    <Row>
                        <Col md="5">
                            <h3>{recipes[index].name}</h3>
                            <p className="mb-2 text-muted">{"Kr " + recipes[index].price}</p>
                        </Col>
                        <Col md="auto">
                            <Row>
                                <Col md="auto">
                                    <Fab size="small"  type="button" onClick={() => handleAmountUpdate(index, -1)}><Remove/>
                                    </Fab>
                                </Col>
                                <Col md="auto">
                                    <h3>{recipes[index].amount}</h3>
                                </Col>
                                <Col md="auto">
                                    <Fab size="small"  type="button" onClick={() => handleAmountUpdate(index, 1)}>
                                        <Add/>
                                    </Fab>
                                </Col>
                            </Row>
                        </Col>
                        <Col>
                            <div style={{display: "flex", flexDirection: "row-reverse"}}>
                                <h3>{"Kr " + (recipes[index].price * recipes[index].amount)}</h3>
                            </div>
                            <div style={{display: "flex", flexDirection: "row-reverse"}}>
                                <IconButton onClick={() => handleRemoveRecipe(index)}>
                                    <DeleteOutline/>
                                </IconButton>
                            </div>
                        </Col>
                    </Row>
                </ListGroup.Item>);
        })
        return list;
    }

    function getDropdownButtons(){
        let dropdownButtons = [];
        allRecipes.forEach(recipe => {
            dropdownButtons.push(
                <DropdownItem as="button" onClick={() => handleNewRecipe(recipe)}>
                    {recipe.name}
                </DropdownItem>
            )
        });
        return dropdownButtons;
    }
    const buttons = getDropdownButtons();
    return(
        <Card style={{ width: '40rem' }}>
            <Card.Body>
                <Card.Title>{barTab.name}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">{barTab.dateCreated.toDateString()}</Card.Subtitle>
                <ListGroup variant="flush">
                    {getRecipes(recipes)}
                    <ListGroupItem>
                        <DropdownButton id="dropdown-item-button" title="Add Recipe">
                            {buttons}
                        </DropdownButton>
                    </ListGroupItem>
                </ListGroup>

            </Card.Body>
            <Card.Footer>
                <Row>
                    <Col>
                        <h4>Paid ?</h4>
                        <Switch
                            checked={paid}
                            onChange={() => setPaid(!paid)}
                            color="primary"
                            name="checkedB"
                        />
                    </Col>
                    <Col md="auto">
                        <div style={{display: "flex", flexDirection: "row-reverse"}}>
                            <h3>{"Total: " + getSum(recipes) + "kr"}</h3>
                        </div>
                    </Col>
                </Row>
            </Card.Footer>
        </Card>
    );
}




function getSum(recipes){
    let sum = 0;
    recipes.forEach(recipe => {
        sum += recipe.price * recipe.amount;
    })
    return sum;
}