import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
import EntryAccordion from "./EntryAccordion";

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

const styles = {
  width: 420,
  position: "absolute",
  top: 70,
  left: "50%",
  WebkitTransform: "translate(-50%, 0)",
  transform: "translate(-50%, 0)"
}

function Result() {
  let query = useQuery();
  let [people, setPeople] = useState([]);

  useEffect(() => {
    fetch(`http://localhost:8080/result/${query.get("id")}?depth=${query.get("depth")}&width=${query.get("width")}`)
      .then(() => {
        const interval = setInterval(() => {
          fetch(`http://localhost:8080/refresh`)
            .then(res => res.json())
            .then(response => {
                setPeople(response.result.map(entry => (<EntryAccordion key={entry.user.id} person={entry.user}
                                                                       friends={entry.friends}/>)))
              console.log("refresh")
              }
            )
        }, 5000);
        return clearInterval(interval);
      })
  }, [query])

  return (
    <div style={styles}>{people}</div>
  );
}

export default Result;