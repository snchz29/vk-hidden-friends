import "./Form.css"
import searchImage from "../img/search.png"
import React from "react";
import {Button} from "@material-ui/core";
import RadioFields from "./RadioFields"
import Context from "../Context";

export default class Form extends React.Component {
  state = {vkId: 147946476, depth: 3, width: 5};

  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this)
    this.setDepth = this.setDepth.bind(this)
    this.setWidth = this.setWidth.bind(this)
    this.setVkId = this.setVkId.bind(this)
  }

  handleSubmit(event) {
    event.preventDefault();
    window.location = "http://localhost:3000/"
  }

  setVkId(val) {
    this.setState({
      vkId: parseInt(val)
    })
  }

  setDepth(val) {
    this.setState({
      depth: parseInt(val)
    })
  }

  setWidth(val) {
    this.setState({
      width: parseInt(val)
    })
  }


  render() {
    return (
      <Context.Provider value={{state: this.state, setDepth: this.setDepth, setWidth: this.setWidth}}>
        <form action={"/result"}>
          <legend>Id</legend>
          <input name="id" min="1" placeholder="EntryAccordion ID" type="number" defaultValue={147946476}
                 onChange={(e) => this.setVkId(e.target.value)}/>
          <RadioFields/>
          <Button id="run" type="submit">
            <img style={{width: 30}} src={searchImage} alt="Submit"/>
          </Button>
        </form>
      </Context.Provider>
    );
  }
}