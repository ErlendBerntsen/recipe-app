import React, {useState} from 'react';
import {MasterPage} from "./MasterPage";
import {Button, Col, Form, Modal, Row, Spinner} from "react-bootstrap";
import {Formik} from "formik";
import BarTabCard from "../components/BarTabCard";
import useSWR from "swr";
import {getAllRecipes} from "../api/Methods";
import {MyTextInput} from "../components/RecipeForm";
import * as Yup from 'yup';

let recipes;
export default function BarTabsPage () {
    const [show, setShow] = useState(false);
    const [barTabs, setBarTabs] = useState([]);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    let recipeFetchDisplay = GetRecipes(show, setShow, handleClose, handleShow, barTabs, setBarTabs);

    return(
        <MasterPage>
            <br/>
            {recipeFetchDisplay}
            <br/>
            <br/>
            <Row>
                {barTabs}
            </Row>
        </MasterPage>
    );
}

function GetRecipes(show, setShow, handleClose, handleShow, barTabs, setBarTabs){
    const{data, error} = useSWR('/api/recipes', getAllRecipes())
    if(error){
        return <p>Error in loading data</p>
    }else if(!data){
        return (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
    }else{
        recipes = data._embedded.recipeList;
        return (GetCreateButton(show, setShow, handleClose, handleShow, barTabs, setBarTabs));
    }
}

function GetCreateButton(show, setShow, handleClose, handleShow, barTabs, setBarTabs){

    return(
        <>
            <Button onClick={() => handleShow()}> Create New Bar Tab</Button>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Bar Tab</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Formik
                        initialValues={{name : ''}}
                        validationSchema={Yup.object({
                            name: Yup.string().required('Required'),
                        })}
                        onSubmit={(values, {setSubmitting}) => {
                            setTimeout(() => {
                                setSubmitting(false);
                            }, 400);

                            const newBarTab = {
                                name: values.name,
                                recipes: [],
                                dateCreated: new Date(),
                                paid: false,
                            }
                            const newBarTabCard = <Col> <BarTabCard barTab={newBarTab} allRecipes={recipes}/> </Col>
                            setBarTabs(prevRecipes => [...prevRecipes, newBarTabCard]);
                            handleClose();
                        }}
                    >
                        {formik => (
                            <Form onSubmit={formik.handleSubmit}>
                                <Form.Label><h3>Name</h3></Form.Label>
                                <Form.Group>
                                    <MyTextInput
                                        label="Name"
                                        name="name"
                                        type="text"
                                        placeholder="Enter name"
                                    />
                                </Form.Group>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={handleClose}>
                                        Close
                                    </Button>
                                    <Button type="submit" variant="primary">
                                        Create Bar Tab
                                    </Button>
                                </Modal.Footer>
                            </Form>
                        )}
                    </Formik>
                </Modal.Body>
            </Modal>
        </>
    );
}