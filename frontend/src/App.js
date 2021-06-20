import React, { Component } from "react";
import { Route, Switch} from "react-router-dom";
import HomePage from "./pages/HomePage";
import IngredientsPage from "./pages/IngredientsPage";
class App extends Component {
  render() {
    return (
      <Switch>
        <Route exact path="/" component={HomePage} />
        <Route exact path="/ingredients" component={IngredientsPage} />
      </Switch>
    );
  }
}



export default App;
