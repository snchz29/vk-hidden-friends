import React from 'react';
import {FormControlLabel, Radio, RadioGroup} from "@material-ui/core";
import PropTypes from 'prop-types'

function Radios({name, values, setter, state}) {
  return (
    <div>
      <legend>{name.charAt(0).toUpperCase() + name.substring(1)}</legend>
      <RadioGroup name={name}>
        <FormControlLabel value={values[0]}
                          control={<Radio color="default"/>}
                          label={"Minimal " + name}
                          onChange={setter.bind(null, values[0])}
                          checked={state === values[0]}/>
        <FormControlLabel value={values[1]}
                          control={<Radio color="default"/>}
                          label={"Middle " + name}
                          onChange={setter.bind(null, values[1])}
                          checked={state === values[1]}/>
        <FormControlLabel value={values[2]}
                          control={<Radio color="default"/>}
                          label={"Maximum " + name}
                          onChange={setter.bind(null, values[2])}
                          checked={state === values[2]}/>
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