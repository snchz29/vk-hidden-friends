import React, {useContext} from "react";
import Context from "../Context";
import Radios from "./Radios";

function RadioFields() {
  const {state, setDepth, setWidth} = useContext(Context);
  return (
    <div className={"radios"}>
      <Radios name={'depth'} values={[3, 4, 5]} setter={setDepth} state={state.depth}/>
      <Radios name={'width'} values={[5, 10, 15]} setter={setWidth} state={state.width}/>
    </div>
  );
}

export default RadioFields;