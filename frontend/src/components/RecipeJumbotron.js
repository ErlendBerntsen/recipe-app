import React, {useState} from "react";
import {Col, Image, Jumbotron, ListGroup, ListGroupItem, Modal, Navbar, ProgressBar, Row} from "react-bootstrap";
import {DeleteForever, Edit} from "@material-ui/icons";
import {Button} from "@material-ui/core";
import {Link} from "react-router-dom";
import {deleteRecipe} from "../api/Methods";

export default function RecipeJumbotron(props){
    const recipe = props.recipe;
    const imageNameAndDescription = getImageNameAndDescription(recipe)
    const basicInfo = getBasicInfo(recipe);
    const ingredients = getIngredients(recipe.ingredients);
    const steps = getSteps(recipe.steps);
    const notes = getNotes(recipe.notes);
    const buttons = GetButtons(recipe);

    return(
        <Jumbotron>
            {imageNameAndDescription}
            {basicInfo}
            {ingredients}
            {steps}
            {notes}
            {buttons}
        </Jumbotron>
    );
}

function getImageNameAndDescription(recipe){
    return(
        <>
            <Image style={{  display: "block", marginLeft: "auto", marginRight: "auto"}} rounded alt="Recipe image" src="../recipes/12.jpg"/>
            <br/>
            <h1>{recipe.name}</h1>
            <h5>{recipe.description}</h5>
        </>
    );
}

function getBasicInfo(recipe){
    const priceText = recipe.price + "kr";
    const difficultyText = recipe.difficulty + "/10";
    const ratingText = recipe.rating + "/10";

    const ratingColor = getColor(recipe.rating);
    const difficultyColor = getColor(10 - recipe.difficulty);
    const ratingBar = <ProgressBar  now={recipe.rating * 10} variant={ratingColor} />
    const difficultyBar = <ProgressBar  now={recipe.difficulty * 10} variant={difficultyColor} />

    function getColor(number){
        if(number >= 7) return "success";
        else if(number > 3) return "warning";
        else return "danger";
    }

    return(
        <>
            <br/>
            <ListGroup>
                <ListGroupItem>
                    <Row>
                        <Col>
                            <p>Portions</p>
                            <h3>{recipe.portions}</h3>
                        </Col>
                        <Col>
                            <p>Glass</p>
                            <h3>{recipe.glass}</h3>
                        </Col>
                        <Col>
                            <p>Price</p>
                            <h3>{priceText}</h3>
                        </Col>
                        <Col>
                            <p>Rating</p>
                            <h3>{ratingText}</h3>
                            {ratingBar}
                        </Col>
                        <Col>
                            <p>Difficulty</p>
                            <h3>{difficultyText}</h3>
                            {difficultyBar}
                        </Col>
                    </Row>
                </ListGroupItem>
            </ListGroup>
        </>
    );
}

function getIngredients(ingredients){
    let ingredientList = [];
    let garnishList = []
    ingredients.forEach(ingredient => {
        let info = ingredient.amount  + ingredient.unit.abbreviation + " " + ingredient.name;
        if(!ingredient.garnish){
            info += " (" + ingredient.ingredient.name + ")"
            ingredientList.push(<ListGroup.Item>{info}</ListGroup.Item>);
        }else{
            garnishList.push(<ListGroup.Item>{info}</ListGroup.Item>)
        }
    });

    return (
        <>
            <br/>
            <Navbar bg="light">
                <h3>Ingredients</h3>
            </Navbar>
            <ListGroup variant="flush">
                {ingredientList}
            </ListGroup>
            <Navbar bg="light">
                <h5>Garnish</h5>
            </Navbar>
            <ListGroup variant="flush">
                {garnishList}
            </ListGroup>
        </>
    );
}

function getSteps(steps){
    const stepsList = steps.split('|');
    let list = [];
    stepsList.forEach((step, index) => {
        const text = (index + 1) + ". " + step
        list.push(<ListGroupItem>{text}</ListGroupItem>)
    })
    return(
        <>

            <br/>

            <Navbar bg="light">
                <h3>Steps</h3>
            </Navbar>
            <ListGroup >
                {list}
            </ListGroup>
        </>
    );
}




function getNotes(notes){
    return (
        <>

            <br/>
            <Navbar bg="light">
                <h3>Notes</h3>
            </Navbar>
            <ListGroup variant="flush">
                <ListGroupItem>{notes} </ListGroupItem>
            </ListGroup>
        </>
    );
}

function GetButtons(recipe){
    const [showDeleteWarning, setShowDeleteWarning] = useState(false);
    const handleCloseDeleteWarning = () => setShowDeleteWarning(false);
    const handleShowDeleteWarning = () => setShowDeleteWarning(true)
    return (
        <>
            <br/>
            <br/>
            <Row>
                <Col>
                    <Link as={Button} to={"/editrecipe/" + recipe.id}>
                        <div style={{float: "right"}}>
                            <Button variant="contained" color="primary" startIcon={<Edit/>}>
                                Edit
                            </Button>
                        </div>
                    </Link>
                </Col>
                <Col >
                    <div >
                        <Button variant="contained" color="secondary" startIcon={<DeleteForever/>} onClick={() => handleShowDeleteWarning()}>
                            Delete
                        </Button>
                    </div>

                </Col>
            </Row>

            <Modal show={showDeleteWarning} onHide={handleCloseDeleteWarning}>
                <Modal.Header closeButton>
                    <Modal.Title>Warning</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure you want to delete this recipe? This action can't be undone.</Modal.Body>
                <Modal.Footer>
                    <Button variant="contained" color="default" onClick={handleCloseDeleteWarning}>
                        Close
                    </Button>
                    <Button variant="contained" color="secondary" onClick={() => handleDelete(recipe, handleCloseDeleteWarning)}>
                        Delete recipe
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}


function handleDelete(recipe, handleCloseDeleteWarning){
    deleteRecipe('/api/recipes/', recipe.id)
        .then(response => {
            response.ok? alert("Deleted successfully") : alert("Error in deletion");
            handleCloseDeleteWarning();
            window.location.reload();
        });
}
