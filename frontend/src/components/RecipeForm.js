import React, {useState} from "react"
import {Formik, Field, useField, FieldArray, insert} from "formik";
import {Button, ButtonGroup, Col, Form, Row, Spinner} from "react-bootstrap";
import * as Yup from 'yup';
import {Fab, Slider} from "@material-ui/core";
import {Add, Clear} from "@material-ui/icons";
import useSWR from "swr";
import {getAllIngredients, getAllUnits} from "../api/Methods";

let units;
export default function RecipeForm(props){
    let unitsFetchDisplay = GetUnits();
    let ingredientsFetchDisplay;
    if(units !== null){
        ingredientsFetchDisplay = GetIngredients(props)
    }
    return (
        units === null? unitsFetchDisplay : ingredientsFetchDisplay
    );
}

const MyTextInput = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return(
      <>
          <Form.Label>{label}</Form.Label>
          <Form.Control as={Field} {...field} {...props}/>
          {meta.touched && meta.error ?
              <div style={{color: "#fc8181"}}>{meta.error}</div> : null
          }
      </>
    );
}

const MyMultipleInput = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return(
        <>
            <Form.Control as={Field} {...field} {...props}/>
            {meta.touched && meta.error ?
                <div style={{color: "#fc8181"}}>{meta.error}</div> : null
            }
        </>
    );
}

const MySelect = ({ label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <>
            <Form.Label>{label}</Form.Label>
            <Form.Control as="select" {...field} {...props} />
            {meta.touched && meta.error ?
                <div>{meta.error}</div> : null}
        </>

    );
};

const MyCheckBox = ({children, ...props}) => {
    const [field, meta] = useField(props);
    return(
        <div>
            <label className="checkbox-input">
                <input type="checkbox" {...field} {...props}/>
                {" " + children}
            </label>
            {meta.touched && meta.error ? (
                <div className="error">{meta.error}</div>
            ) : null}
        </div>
    );
};

function GetUnits(){
    const {data, error} = useSWR('/api/units', getAllUnits())
    if(error) {
        return <p>Error in loading unit data</p>;
    }else if(!data){
        return (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
    }else{
        units = data;
        return <p>Got units</p>
    }
}


function GetIngredients(props){
    const {data, error} = useSWR('/api/ingredients', getAllIngredients())

    if(error){
        return <p>Error in loading ingredients</p>;
    }else if(!data){
        return  (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        );
    }else{
        return <RecipeFormik units={units} ingredients={data._embedded.ingredientList} recipe={props.recipe} />
    }
}

function RecipeFormik(props){

    const allUnits = props.units.map(unit => {
        return <option>{unit.abbreviation}</option>
    })

    const allIngredients = props.ingredients.map(ingredient => {
        return <option>{ingredient.name}</option>
    })

    const isCreating = props.recipe === undefined;

    return(
        <Formik
            initialValues={{
                recipeName: isCreating ? '' : props.recipe.name,
                description: isCreating ? '' : props.recipe.description,
                portions: isCreating? 1 : props.recipe.portions,
                glass: isCreating? '' : props.recipe.glass,
                rating: isCreating? null : props.recipe.rating,
                difficulty: isCreating? null : props.recipe.difficulty,
                steps: isCreating? [''] : props.recipe.steps.split('|'),//TODO add validation and initial value for edit
                notes: isCreating? '' : props.recipe.notes,
                ingredients: [
                    {
                        ingredientName: '',
                        ingredientType: null,
                        amount: null,
                        unit: null,
                        isGarnish: false,
                    }
                ]
            }}
            validationSchema={Yup.object({
                recipeName: Yup.string().required('Required'),
                description: Yup.string().optional(),
                portions: Yup.number().required('Required').integer('Must be a whole number').positive('Must be a positive whole number'),
                glass: Yup.string().optional(),
                rating: Yup.number().optional().max(10, "Rating can't be higher than 10").min(0, "Rating can't be lower than 0"),
                difficulty: Yup.number().optional().max(10, "Difficulty can't be higher than 10").min(0, "Difficulty can't be lower than 0"),
                notes: Yup.string().optional(),
            })}
            onSubmit={(values, {setSubmitting}) => {
                let stepsAsString = "";
                values.steps.forEach((step, index) =>{
                    if(step !== ''){
                        stepsAsString += step;
                        if(index !== (values.steps.length - 1)){
                            stepsAsString += "|";
                        }
                    }
                })
                console.log(values.steps);
                console.log(stepsAsString);
                setTimeout(() => {
                    alert(JSON.stringify(values, null, 2));
                    setSubmitting(false);
                }, 400);
            }}
        >
            {formik => (
                <Form onSubmit={formik.handleSubmit}>
                    <Row>
                        <Col>
                            <Form.Label><h3>Recipe</h3></Form.Label>
                            <Form.Group>
                                <MyTextInput
                                    label="Recipe Name"
                                    name="recipeName"
                                    type="text"
                                    placeholder="Enter name"
                                />
                            </Form.Group>
                            <Form.Group>
                                <MyTextInput
                                    label="Portions"
                                    name="portions"
                                    type="number"
                                />
                            </Form.Group>
                            <Form.Group>
                                <MyTextInput
                                    label="Description (optional)"
                                    name="description"
                                    type="text"
                                    placeholder="Enter description"
                                />
                            </Form.Group>
                            <Form.Group>
                                <MyTextInput
                                    label="Glass (optional)"
                                    name="glass"
                                    type="text"
                                    placeholder="Enter glass"
                                />
                            </Form.Group>
                            <Form.Group>
                                <MyTextInput
                                    label="Rating (optional)"
                                    name="rating"
                                    type="number"
                                    placeholder="Enter rating"
                                />
                            </Form.Group>
                            <Form.Group>
                                <MyTextInput
                                    label="Difficulty (optional)"
                                    name="difficulty"
                                    type="number"
                                    placeholder="Enter difficulty"
                                />
                            </Form.Group>
                            <Form.Group>
                                <Form.Label>Steps (optional)</Form.Label>
                                <FieldArray name="steps">
                                    {({insert, remove, push}) => (
                                        <div>
                                            {formik.values.steps.length > 0 &&
                                            formik.values.steps.map((step, index) => (
                                                <div key={index}>
                                                    <Row>
                                                        <Col md="auto" >
                                                            <p>{index + 1 }</p>
                                                        </Col>
                                                        <Col>
                                                            <MyMultipleInput
                                                                label={"Step " + (index + 1)}
                                                                name={`steps.${index}`}
                                                                type="text"
                                                                placeholder="Enter a step"
                                                            >
                                                            </MyMultipleInput>
                                                        </Col>
                                                        <Col md="auto">
                                                            <Fab size="small" color="secondary" type="button" onClick={() => remove(index)}>
                                                                <Clear />
                                                            </Fab>
                                                        </Col>
                                                    </Row>
                                                </div>
                                            ))}
                                            <br/>
                                            <Fab variant="extended" size="small" color="primary" type="button" onClick={() => push('')}>
                                                <Add />
                                                Add Step
                                            </Fab>
                                        </div>
                                    )}
                                </FieldArray>
                            </Form.Group>
                            <Form.Group>
                                <MyTextInput
                                    label="Notes (optional)"
                                    name="notes"
                                    type="text"
                                    placeholder="Enter notes"
                                />
                            </Form.Group>
                            <Button type="submit">Submit</Button>
                        </Col>
                        <Col>
                            <Form.Group>
                                <Form.Label><h3> Ingredients</h3> </Form.Label>
                                <FieldArray name="ingredients">
                                    {({insert, remove, push}) => (
                                        <div>
                                            {formik.values.ingredients.length > 0 &&
                                            formik.values.ingredients.map((ingredient, index) => (
                                                <div key={index}>
                                                    <Row>
                                                        <Col>
                                                            <Form.Group>
                                                                <Row>
                                                                    <Col>
                                                                        <MyTextInput
                                                                            label="Ingredient Name"
                                                                            name={`ingredient.${index}.ingredientName`}
                                                                            type="text"
                                                                            placeholder="Enter ingredient name"
                                                                        />
                                                                    </Col>
                                                                    <Col>
                                                                        <MySelect
                                                                            label="Ingredient Type"
                                                                            name={`ingredient.${index}.ingredientType`}
                                                                        >
                                                                            <option value="">Select an ingredient</option>
                                                                            {allIngredients}
                                                                        </MySelect>
                                                                    </Col>
                                                                </Row>
                                                            </Form.Group>
                                                            <Form.Group>
                                                                <Row>
                                                                    <Col>
                                                                        <MyTextInput
                                                                            label="Amount"
                                                                            name={`ingredient.${index}.amount`}
                                                                            type="number"
                                                                            placeholder="Enter amount"
                                                                        />
                                                                    </Col>
                                                                    <Col>
                                                                        <MySelect
                                                                            label="Unit"
                                                                            name={`ingredient.${index}.unit`}
                                                                        >
                                                                            <option value="">Select an unit</option>
                                                                            {allUnits}
                                                                        </MySelect>
                                                                    </Col>
                                                                </Row>
                                                                <MyCheckBox name={`ingredient.${index}.isGarnish`}>
                                                                    Add as garnish
                                                                </MyCheckBox>

                                                            </Form.Group>
                                                        </Col>
                                                        <Col md="auto">
                                                            <Fab size="small" color="secondary" type="button" onClick={() => remove(index)}>
                                                                <Clear />
                                                            </Fab>
                                                        </Col>
                                                    </Row>
                                                    <hr/>
                                                </div>
                                            ))}
                                                <Fab variant="extended" size="small" color="primary" type="button" onClick={() => push({
                                                    ingredientName: "",
                                                    ingredientType: null,
                                                    amount: null,
                                                    unit: null,
                                                    isGarnish: false,
                                                })}>
                                                    <Add />
                                                    Add Ingredient
                                                </Fab>
                                        </div>
                                    )}
                                </FieldArray>
                            </Form.Group>
                        </Col>
                    </Row>
                </Form>
            )}
        </Formik>
    );
}