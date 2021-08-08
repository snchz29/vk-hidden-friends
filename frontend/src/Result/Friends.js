import React from "react";
import Person from "./Person";

function Friends({array}) {
  let friendsArray = []
  for (let friend of array) {
    friendsArray.push(<Person key={friend.id} person={friend} isHiddenFriend={true}/>)
  }
  return (
    <div style={{width: "100%"}}>{friendsArray}</div>
  )
}

export default Friends;