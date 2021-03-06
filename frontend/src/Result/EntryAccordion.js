import React from "react";
import Person from "./Person";
import PropTypes from "prop-types";
import Friends from "./Friends";
import {Accordion, AccordionDetails, AccordionSummary} from "@material-ui/core";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';

function EntryAccordion({person, friends}) {
  return (
    <Accordion style={{backgroundColor: "rgba(0,0,0,0)", borderRadius: "1rem", margin: 0}}>
      <AccordionSummary
        expandIcon={<ExpandMoreIcon/>}
        style={{backgroundColor: "rgba(0,0,0,0.2)", borderRadius: "1rem", margin: 0}}>
        <Person person={person}/>
      </AccordionSummary>
      <AccordionDetails
        style={{backgroundColor: "rgba(0,0,0,0.5)", borderRadius: "1rem", height: "100%"}}>
        <Friends array={friends}/>
      </AccordionDetails>
    </Accordion>
  );
}

EntryAccordion.propTypes = {
  person: PropTypes.object.isRequired,
  friends: PropTypes.array.isRequired
}

export default EntryAccordion;