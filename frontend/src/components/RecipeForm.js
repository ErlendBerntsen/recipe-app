import React from "react"
import {Formik, Field, useField, FieldArray} from "formik";
import { Button, Col, Form, Row, Spinner} from "react-bootstrap";
import * as Yup from 'yup';
import {Fab} from "@material-ui/core";
import {Add, Clear} from "@material-ui/icons";
import useSWR from "swr";
import {
    createNewRecipe,
    editRecipe,
    getAllIngredients,
    getAllUnits
} from "../api/Methods";
import {RecipeDTO, RecipeIngredientDTO} from "../api/Classes";
import {useHistory} from "react-router";

let units;
const errorColor = "red";

export default function RecipeForm(props){
    let unitsFetchDisplay = GetUnits();
    let ingredientsFetchDisplay;
    if(units !== null){
        ingredientsFetchDisplay = GetIngredients(props)
    }
    return (
        <>
            {units === null? unitsFetchDisplay : ingredientsFetchDisplay}
        </>
    );
}

const MyTextInput = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return(
      <>
          <Form.Label>{label}</Form.Label>
          {meta.touched && meta.error ?
              <>
                  <Form.Control style={{borderColor: "red"}} as={Field} {...field} {...props}/>
                  <div style={{color: "red"}}>{meta.error}</div>
              </>
              : <Form.Control as={Field} {...field} {...props}/>
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
                <div style={{color: errorColor}}>{meta.error}</div> : null
            }
        </>
    );
}

const MySelect = ({ label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <>
            <Form.Label>{label}</Form.Label>
            {meta.touched && meta.error ?
                <>
                    <Form.Control style={{borderColor: "red"}} as="select" {...field} {...props} />
                    <div style={{color: errorColor}}>{meta.error}</div>
                </>
                : <Form.Control  as="select" {...field} {...props} />
            }
        </>

    );
};

const MyCheckBox = ({children, ...props}) => {
    const [field, meta] = useField(props);
    return(
        <div>
            <label className="checkbox-input">
                <input type="checkbox" checked={meta.initialValue} {...field} {...props} />
                {" " + children}
            </label>
            {meta.touched && meta.error ? (
                <div style={{color: errorColor}}>{meta.error}</div>
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

    let ingredientList = [];
    if(!isCreating){
        props.recipe.ingredients.forEach(ingredient => {
            ingredientList.push(
                {
                    ingredientName: ingredient.name,
                    ingredientType: ingredient.ingredient.name,
                    amount: ingredient.amount,
                    unit: ingredient.unit.abbreviation,
                    isGarnish: ingredient.garnish,
                }
            );
        });
    }

    return(
        <Formik
            initialValues={{
                recipeName: isCreating ? '' : props.recipe.name,
                portions: isCreating? 1 : props.recipe.portions,
                description: isCreating ? '' : props.recipe.description,
                steps: isCreating? [''] : props.recipe.steps.split('|'),
                notes: isCreating? '' : props.recipe.notes,
                glass: isCreating? '' : props.recipe.glass,
                rating: isCreating? undefined : props.recipe.rating,
                difficulty: isCreating? undefined : props.recipe.difficulty,
                ingredients: isCreating ? [
                    {
                        ingredientName: '',
                        ingredientType: undefined,
                        amount: undefined,
                        unit: undefined,
                        isGarnish: undefined,
                    }
                ] : ingredientList,
            }}
            validationSchema={Yup.object({
                recipeName: Yup.string().required('Required'),
                portions: Yup.number().required('Required').integer('Must be a whole number').positive('Must be a positive whole number'),
                description: Yup.string().optional(),
                steps: Yup.array(Yup.string()).optional(),
                notes: Yup.string(),
                glass: Yup.string().optional(),
                rating: Yup.number().optional().max(10, "Rating can't be higher than 10").min(0, "Rating can't be lower than 0"),
                difficulty: Yup.number().optional().max(10, "Difficulty can't be higher than 10").min(0, "Difficulty can't be lower than 0"),
                ingredients: Yup.array(Yup.object().shape({
                    ingredientName: Yup.string().required('Required'),
                    ingredientType: Yup.string().required('Required'),
                    amount: Yup.number().required('Required').positive('Must be a positive number'),
                    unit: Yup.string().required('Required'),
                    isGarnish: Yup.boolean().optional(),
                })).min(1, 'Recipe must have at least one ingredient'),
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
                let ingredients = []
                values.ingredients.forEach(ingredient =>{
                    ingredients.push(new RecipeIngredientDTO(ingredient.ingredientName, ingredient.ingredientType,
                        ingredient.amount, ingredient.unit, ingredient.isGarnish))
                })
                const recipe = new RecipeDTO(values.recipeName, values.portions, values.description , stepsAsString,
                    values.notes, values.glass, values.rating, values.difficulty, ingredients)
                isCreating ? HandleCreation(recipe) : HandleEdit(recipe, props.recipe.id)
                setTimeout(() => {
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
                                    {({remove, push}) => (
                                        <div>
                                            {formik.values.steps.length > 0 &&
                                            formik.values.steps.map((step, index) => (
                                                <div key={index}>
                                                    <Row>
                                                        <Col md="auto" style={{display: "flex", alignItems: "center"}}>
                                                            {index + 1 }
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
                        </Col>
                        <Col>
                            <Form.Group>
                                <Form.Label><h3> Ingredients</h3> </Form.Label>
                                {
                                    typeof formik.errors.ingredients === 'string' ?
                                        <div style={{color: errorColor}}>{formik.errors.ingredients}</div> : null
                                }
                                <FieldArray name="ingredients">
                                    {({remove, push}) => (
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
                                                                            name={`ingredients.${index}.ingredientName`}
                                                                            type="text"
                                                                            placeholder="Enter ingredient name"
                                                                        />
                                                                    </Col>
                                                                    <Col>
                                                                        <MySelect
                                                                            label="Ingredient Type"
                                                                            name={`ingredients.${index}.ingredientType`}
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
                                                                            name={`ingredients.${index}.amount`}
                                                                            type="number"
                                                                            placeholder="Enter amount"
                                                                        />
                                                                    </Col>
                                                                    <Col>
                                                                        <MySelect
                                                                            label="Unit"
                                                                            name={`ingredients.${index}.unit`}
                                                                        >
                                                                            <option value="">Select an unit</option>
                                                                            {allUnits}
                                                                        </MySelect>
                                                                    </Col>
                                                                </Row>
                                                                <MyCheckBox name={`ingredients.${index}.isGarnish`}>
                                                                    Add as garnish
                                                                </MyCheckBox>

                                                            </Form.Group>
                                                        </Col>
                                                        <Col md="auto" style={{display: "flex", alignItems: "center", }}>
                                                            <div >
                                                                <Fab size="small" color="secondary" type="button" onClick={() => remove(index)}>
                                                                    <Clear />
                                                                </Fab>
                                                            </div>
                                                        </Col>
                                                    </Row>
                                                    <hr/>
                                                </div>
                                            ))}
                                                <Fab variant="extended" size="small" color="primary" type="button" onClick={() => push({
                                                    ingredientName: "",
                                                    ingredientType: undefined,
                                                    amount: undefined,
                                                    unit: undefined,
                                                    isGarnish: undefined,
                                                })}>
                                                    <Add />
                                                    Add Ingredient
                                                </Fab>
                                        </div>
                                    )}
                                </FieldArray>
                            </Form.Group>
                            <div style={{display: "flex", flexDirection: "row-reverse"}}>
                                <Button  disabled={formik.isSubmitting} type="submit">{isCreating ? "Create Recipe" : "Save Changes"}</Button>
                            </div>
                        </Col>
                    </Row>
                </Form>
            )}
        </Formik>
    );
}

function HandleCreation(recipe){
    createNewRecipe('/api/recipes', JSON.stringify(recipe))
        .then(HandleResponse);
}

function HandleEdit(recipe, recipeId){
    let patch = '['
    Object.entries(recipe).forEach(([key, value]) => {
        if(key === 'ingredients'){
            value.forEach((ingredient, index) => {
                Object.entries(ingredient).forEach(([iKey, iValue]) => {
                    patch += '{"op": "replace", "path": "/' + key + '/' + index + '/' +   iKey    +'", "value": "' + iValue + '"},'
                });
            });
        }else{
            patch += '{"op": "replace", "path": "/' + key + '", "value": "' + value + '"},'
        }
    });

    const actualPatch = patch.substring(0, patch.length-2) + '}]';
    editRecipe('/api/recipes/', recipeId, actualPatch)
        .then(HandleResponse);
}

function HandleResponse(response){
    response.ok? alert("success") : alert("error")
}

function Redirect(response){
    let history = useHistory();
    alert("success");
    response.json().then(data => {
        history.push('/recipes/' + data.id);
    })
}