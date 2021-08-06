import './App.css';
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";
import Form from "./Form/Form";
import Login from "./Login";

export default function App() {
  return (
    <Router>
      <nav>
        <Link id="title" to="/">Hidden Friends</Link>
        <a id="login"
           href="https://oauth.vk.com/authorize?client_id=7900610&display=popup&redirect_uri=http://localhost:3000/login&scope=friends,groups,photos&response_type=code&v=5.120">Login</a>
      </nav>
      <Switch>
        <Route path="/login">
          <Login/>
        </Route>
        <Route path="/">
          <Form/>
        </Route>
      </Switch>
    </Router>
  );
}