import './App.css';
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";
import Form from "./Form";

function App() {
    return (
        <Router>
            <nav>
                <Link id="title" to="/">Hidden Friends</Link>
                <Link id="login" to="/login">Login</Link>
            </nav>
            <Switch>
                <Route path="/">
                    <Form/>
                </Route>
                <Route path="/login">
                    {/*"https://oauth.vk.com/authorize?client_id=7900610&display=popup&redirect_uri=http://localhost:8080/login&scope=friends,groups,photos&response_type=code&v=5.120"*/}
                </Route>
            </Switch>
        </Router>
    );
}

export default App;
