import React from "react";
import PropTypes from "prop-types"
import vkLogo from '../img/vk-logotype.png'

const styles = {
  mainDiv: {font: "inherit", fontSize: "x-large", width: "100%", height: "100%", cursor: "pointer"},
  linkImg: {width: 30},
  link: {position: "absolute", bottom: 0, right: 0},
  leftPerson: {
    image: {width: 150, borderRadius: "2rem", float: "left"},
    name: {marginLeft: "1rem", float: "left"}
  },
  rightPerson: {
    image: {marginLeft: "1rem", width: 50, borderRadius: "1rem", float: "right"},
    name: {float: "right"}
  }
}

function Person({person, isHiddenFriend}) {
  const logo = (
    <a href={`https://vk.com/id${person.id}`}
       target="_blank"
       rel="noreferrer"
       style={styles.link}>
      <img src={vkLogo}
           alt={"Vk page"}
           style={styles.linkImg}/>
    </a>)

  function openVk() {
    if (isHiddenFriend)
      window.open(`https://vk.com/id${person.id}`, '_blank').focus()
  }

  return (
    <div style={styles.mainDiv} onClick={openVk}>
      <img src={person.photoUri}
           alt={person.firstName + " " + person.lastName}
           style={isHiddenFriend ? styles.rightPerson.image : styles.leftPerson.image}/>
      <div style={isHiddenFriend ? styles.rightPerson.name : styles.leftPerson.name}>
        {person.firstName} {person.lastName}
      </div>
      {!isHiddenFriend && logo}
    </div>
  )
}

Person.propTypes = {
  person: PropTypes.object.isRequired,
  isHiddenFriend: PropTypes.bool
}

export default Person;