import React, {useState} from 'react';
import {MasterPage} from "./MasterPage";
import {Button} from "react-bootstrap";
import {useHistory} from "react-router";

export default function BarTabsPage () {
    let history = useHistory();
    let [counter, setCounter] = useState(0);
    function handleClick(){
        setCounter(counter + 1);
        if(counter === 3) history.push('/recipes');
    }
    return(
        <MasterPage>
            <Button onClick={() => handleClick()}> Add</Button>
            <h1>{counter}</h1>
        </MasterPage>
    );
}