import React, { Component } from "react";
import { Route, Switch} from "react-router-dom";
import HomePage from "./pages/HomePage";
import IngredientsPage from "./pages/IngredientsPage";
import RecipesPage from "./pages/RecipesPage";
import RecipePage from "./pages/RecipePage";
import CreateRecipePage from "./pages/CreateRecipePage";
import EditRecipePage from "./pages/EditRecipePage";

class App extends Component {
  render() {
    return (
      <Switch>
        <Route exact path="/" component={HomePage} />
        <Route exact path="/ingredients" component={IngredientsPage} />
        <Route exact path="/recipes" component={RecipesPage} />
        <Route exact path="/recipes/:id" component={RecipePage} />
        <Route exact path="/createrecipe" component={CreateRecipePage} />
        <Route exact path="/editrecipe/:id" component={EditRecipePage} />
      </Switch>
    );
  }
}

export default App;
