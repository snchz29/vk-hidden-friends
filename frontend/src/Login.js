import React from "react"
import {Redirect} from "react-router-dom";

export default function Login() {
  const query = new URLSearchParams(window.location.search);
  const code = query.get('code')

  console.log(code)

  fetch("http://localhost:8080/login?code=" + code).then()
  return (
    <Redirect to="/"/>
  )
}