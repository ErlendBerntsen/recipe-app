import {Route, Switch }from 'react-router-dom';
import IngredientsPage from "./IngredientsPage"
function App (){
    return(
        <Switch>
            <Route exact path="/" component={IngredientsPage}></Route>
        </Switch>
    );
}
export default App