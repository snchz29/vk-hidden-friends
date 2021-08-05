import "./Form.css"
import searchImage from "./img/search.png"
import React from "react";
import {Button, FormControl, FormControlLabel, FormLabel, Input, Radio, RadioGroup} from "@material-ui/core";

class Form extends React.Component {

    constructor(props) {
        super(props);

        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();
        console.log("Submit")
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <FormLabel component="legend">Id</FormLabel>
                <Input min="1" placeholder="User ID" type="number" defaultValue="147946476"/>
                <div>
                    <FormControl component="fieldset">
                        <FormLabel component="legend">Depth</FormLabel>
                        <RadioGroup row={true} label="Depth" name="depth">
                            <FormControlLabel value="3" control={<Radio color="default"/>} label="Minimal depth"/>
                            <FormControlLabel value="4" control={<Radio color="default"/>} label="Middle depth"/>
                            <FormControlLabel value="5" control={<Radio color="default"/>} label="Maximum depth"/>
                        </RadioGroup>

                        <FormLabel component="legend">Width</FormLabel>
                        <RadioGroup row={true} label="Width" name="width">
                            <FormControlLabel value="5" control={<Radio color="default"/>} label="Minimal width"/>
                            <FormControlLabel value="10" control={<Radio color="default"/>} label="Middle width"/>
                            <FormControlLabel value="15" control={<Radio color="default"/>} label="Maximum width"/>
                        </RadioGroup>

                    </FormControl>
                </div>
                <Button id="run" type="submit">
                    <img style={{width: 30}} src={searchImage} alt="Submit"/>
                </Button>
            </form>
        );
    }
}

export default Form;