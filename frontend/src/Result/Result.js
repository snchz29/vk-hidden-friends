import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import Loader from "./Loader"
import EntryAccordion from "./EntryAccordion";

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

const styles = {
  width: 500,
  position: "absolute",
  top: 70,
  left: "50%",
  WebkitTransform: "translate(-50%, 0)",
  transform: "translate(-50%, 0)",
  minHeight: "50px"
}

function Result() {
  let query = useQuery();
  let [isRunning, setIsRunning] = useState(true);
  let [people, setPeople] = useState([]);

  function updateResults() {
    fetch(`http://localhost:8080/refresh`)
      .then(res => res.json())
      .then(response => {
          if (response.result)
            setPeople(response.result.map(entry => (<EntryAccordion key={entry.user.id} person={entry.user}
                                                                    friends={entry.friends}/>)));
          console.log("refresh");
          console.log("isRunning: " + response.isRunning);
          setIsRunning(response.isRunning);
        }
      )
  }

  fetch(`http://localhost:8080/result/${query.get("id")}?depth=${query.get("depth")}&width=${query.get("width")}`).then()

  useEffect(() => {
    const interval = setInterval(updateResults, 25000);
    if (!isRunning) {
      clearInterval(interval)
      console.log("clear interval")
    }
    return () => clearInterval(interval);
  })


  return (
    <div style={styles}>
      {people}
      {isRunning && (<Loader/>)}
    </div>
  );
}

export default Result;