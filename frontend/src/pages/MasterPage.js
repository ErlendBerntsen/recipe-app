import React from "react";
import {Container, Nav, Navbar} from "react-bootstrap";
import {Link} from "react-router-dom";
import {Copyright, LocalBar} from "@material-ui/icons";

export function MasterPage(props){
    return(
        <div>
            <Navbar bg="dark" variant="dark">
                <Navbar.Brand as={Link} to='/'><LocalBar/> </Navbar.Brand>
                <Nav className="mr-auto">
                    <Nav.Link as={Link} to="/" className="nav-link">Home</Nav.Link>
                    <Nav.Link as={Link} to="/ingredients" className="nav-link">Ingredients</Nav.Link>
                    <Nav.Link as={Link} to="/recipes" className="nav-link">Recipes</Nav.Link>
                    <Nav.Link as={Link} to="/bartabs" className="nav-link">Bar Tabs</Nav.Link>
                </Nav>
            </Navbar>
            <main style={{backgroundColor: "#f4f4f5"}}>
                <Container >{props.children}</Container>
            </main>
            <footer>
                    <Container>
                        Made by the incredible talented team at EB productions.
                        <br/>
                        <Copyright> </Copyright> All right reserved
                    </Container>
            </footer>
        </div>
    );
}