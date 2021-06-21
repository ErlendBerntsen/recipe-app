import React from "react";
import {MasterPage} from "./MasterPage";
import {Link} from "react-router-dom";

class HomePage extends React.Component{
    render(){
        return (
            <div>
                <MasterPage />
                <Link to="/testing">Go to testing one page</Link>
                <Link to="/testingtwo">Go to testing two page</Link>
                <Link to="/testingthree">Go to testing three page</Link>
                <h1>This is the homepage!</h1>
            </div>
        );
    }
}
export default HomePage;

