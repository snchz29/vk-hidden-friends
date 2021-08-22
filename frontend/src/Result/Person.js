import React from "react";
import PropTypes from "prop-types"
import vkLogo from '../img/vk-logotype.png'

const styles = {
  mainDiv: {font: "inherit", fontSize: "x-large", width: "100%", height: "100%", cursor: "pointer"},
  linkImg: {width: 30},
  link: {position: "absolute", bottom: 0, right: 0},
  user: {
    image: {width: 150, borderRadius: "2rem"},
  },
  friend: {
    image: {marginLeft: "1rem", width: 50, borderRadius: "1rem"}
  },
  name: {marginLeft: "1rem"}
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
           style={isHiddenFriend ? styles.friend.image : styles.user.image}/>
      <span style={styles.name}>
        {person.firstName} {person.lastName}
      </span>
      {!isHiddenFriend && logo}
    </div>
  )
}

Person.propTypes = {
  person: PropTypes.object.isRequired,
  isHiddenFriend: PropTypes.bool
}

export default Person;