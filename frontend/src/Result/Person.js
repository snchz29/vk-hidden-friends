import React from "react";
import PropTypes from "prop-types"
import vkLogo from '../img/vk-logotype.png'

function Person({person}) {
  return (
    <div style={{font: "inherit", fontSize: "x-large"}}>
      <img src={person.photoUri}
           alt={person.firstName + " " + person.lastName}
           style={{width: 150, borderRadius: "2rem", float: "left"}}/>
      <div style={{marginLeft: "1rem", float: "left"}}>
        {person.firstName} {person.lastName}
      </div>
      <a href={`https://vk.com/id${person.id}`}
         target="_blank"
         rel="noreferrer"
         style={{position: "absolute", bottom: 0, right: 0}}>
        <img src={vkLogo}
             alt={"Vk page"}
             style={{width: 30}}/>
      </a>
    </div>
  )
}

Person.propTypes = {
  person: PropTypes.object.isRequired
}

export default Person;