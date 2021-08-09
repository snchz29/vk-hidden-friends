import React, {useEffect, useState} from "react";
import {useLocation} from "react-router-dom";
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
  transform: "translate(-50%, 0)"
}

function Result() {
  let query = useQuery();
  let [people, setPeople] = useState([]);

  function refresh(){
    fetch(`http://localhost:8080/refresh`)
      .then(res => res.json())
      .then(response => {
          if (response.result)
            setPeople(response.result.map(entry => (<EntryAccordion key={entry.user.id} person={entry.user}
                                                                    friends={entry.friends}/>)))
          console.log("refresh")
        }
      )
  }

  useEffect(() => {
    fetch(`http://localhost:8080/result/${query.get("id")}?depth=${query.get("depth")}&width=${query.get("width")}`)
      .then(response => {
        if (response.result)
          setPeople(response.result.map(entry => (<EntryAccordion key={entry.user.id} person={entry.user}
                                                                  friends={entry.friends}/>)));
        refresh();
      })
      .then(() => {
        const interval = setInterval(refresh, 25000);
        return () => clearInterval(interval);
      })
  }, [query, people])

  return (
    <div style={styles}>{people}</div>
  );
}

export default Result;