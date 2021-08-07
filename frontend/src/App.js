import './App.css';
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";
import Form from "./Form/Form";
import Login from "./Login";
import React, {useEffect, useState} from "react";
import Result from './Result/Result'

export default function App() {
  const [loginData, setLoginData] = useState({isLoggedIn: false, loginLink: ""})

  useEffect(() => {
    fetch('http://localhost:8080/')
      .then(response => response.json())
      .then(data => {
        setLoginData(data)
      })
  }, [])

  return (
    <Router>
      <nav>
        <Link id="title" to="/">Hidden Friends</Link>
        {!loginData.isLoggedIn && <a id="login"
                                     href={loginData.loginLink}>Login</a>}
        {loginData.isLoggedIn && <a id="logout"
                                    href={"http://localhost:8080/logout"}>Logout</a>}

      </nav>
      <Switch>
        <Route path="/login">
          <Login/>
        </Route>
        <Route path="/result">
          <Result/>
        </Route>
        <Route path="/">
          {loginData.isLoggedIn && <Form/>}
        </Route>
      </Switch>
    </Router>
  );
}