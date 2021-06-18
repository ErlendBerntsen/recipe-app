import React from "react";
import {Link} from "react-router-dom";

class HomePage extends React.Component{
    render(){
        return (
            <div>
                <h1>This is the homepage!</h1>
                <br/>
                <Link to="/ingredients"> Go to ingredients page</Link>
            </div>
        );
    }
}
export default HomePage;

