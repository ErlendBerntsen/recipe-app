import React from "react";
import {MasterPage} from "./MasterPage";

class HomePage extends React.Component{
    render(){
        return (
            <div>
                <MasterPage />
                <h1>This is the homepage!</h1>
            </div>
        );
    }
}
export default HomePage;

