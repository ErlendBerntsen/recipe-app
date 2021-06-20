import React from "react";
import {Container, Nav, Navbar} from "react-bootstrap";

export class MasterPage extends React.Component{
    render(){
        return(
            <div>
                <Navbar bg="dark" variant="dark">
                    <Navbar.Brand href="/">Logo</Navbar.Brand>
                    <Nav className="mr-auto">
                        <Nav.Link href="/">Home</Nav.Link>
                        <Nav.Link href="/ingredients">Ingredients</Nav.Link>
                    </Nav>
                </Navbar>
                <main>
                    <Container>{this.props.children}</Container>
                </main>
            </div>
        );
    }


}