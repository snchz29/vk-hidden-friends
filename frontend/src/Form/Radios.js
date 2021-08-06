import React from 'react';
import {FormControlLabel, Radio, RadioGroup} from "@material-ui/core";
import PropTypes from 'prop-types'

function Radios(props) {
  return (
    <div>
      <legend>{props.name.charAt(0).toUpperCase() + props.name.substring(1)}</legend>
      <RadioGroup>
        <FormControlLabel value={props.values[0]}
                          control={<Radio color="default"/>}
                          label={"Minimal " + props.name}
                          onChange={props.setter.bind(null, props.values[0])}
                          checked={props.state === props.values[0]}/>
        <FormControlLabel value={props.values[1]}
                          control={<Radio color="default"/>}
                          label={"Middle " + props.name}
                          onChange={props.setter.bind(null, props.values[1])}
                          checked={props.state === props.values[1]}/>
        <FormControlLabel value={props.values[2]}
                          control={<Radio color="default"/>}
                          label={"Maximum " + props.name}
                          onChange={props.setter.bind(null, props.values[2])}
                          checked={props.state === props.values[2]}/>
      </RadioGroup>
    </div>
  );
}

Radios.propTypes = {
  name: PropTypes.string.isRequired,
  values: PropTypes.array.isRequired,
  setter: PropTypes.func.isRequired,
  state: PropTypes.number
}

export default Radios;