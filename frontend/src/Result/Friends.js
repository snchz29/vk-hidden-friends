import React from "react";
import Person from "./Person";

function Friends({array}){
  let friendsArray = []
  for (let friend of array) {
    friendsArray.push(<Person key={friend.id} person={friend}/>)
  }
  return (
    <div>{friendsArray}</div>
  )
}

export default Friends;