import React from "react";
import {Container, Nav, Navbar} from "react-bootstrap";
import {Link} from "react-router-dom";
import {LocalBar} from "@material-ui/icons";

export function MasterPage(props){
    return(
        <div>
            <Navbar bg="dark" variant="dark">
                <Navbar.Brand as={Link} to='/'><LocalBar/> </Navbar.Brand>
                <Nav className="mr-auto">
                    <Nav.Link as={Link} to="/" className="nav-link">Home</Nav.Link>
                    <Nav.Link as={Link} to="/ingredients" className="nav-link">Ingredients</Nav.Link>
                </Nav>
            </Navbar>
            <main>
                <Container>{props.children}</Container>
            </main>
        </div>
    );
}