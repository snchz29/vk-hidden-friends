import React from "react";
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
    "-webkit-transform": "translate(-50%, 0)",
    transform: "translate(-50%, 0)"
}

function Result() {
  let query = useQuery();
  console.log(query.get("id"));
  let response;

  // useEffect(() => {
  //   fetch(`http://http://localhost:8080/result?id=${query.get("id")}&depth=${query.get("depth")}&width=${query.get("width")}`)
  //     .then(res => res.json())
  //     .then(data => {
  //       response = data;
  //     });
  // }, [])

  response = {
    "loggedIn": true,
    "result": [
      {
        "person": {
          "id": 619270,
          "firstName": "Апосум",
          "lastName": "Raketa",
          "photoUri": "https://sun4-12.userapi.com/s/v1/ig2/E2x0pMfPf1yVli5ursHCMY31uN0j6VF0zUNlb4TKJs-4Cv40kn8Se0tcsFOubA2zi_0ygVk6aCgmsP17IsPkV1nI.jpg?size=200x0&quality=96&crop=0,210,719,719&ava=1"
        },
        "hiddenFriends": [
          {
            "id": 268865915,
            "firstName": "Эрнест",
            "lastName": "Раксин",
            "photoUri": "https://sun4-15.userapi.com/s/v1/ig2/mPONcfJkpUyenNSzvlCFE0nlAA2SjZOtqz2q9FXCPgtR_ESF2dnFiezcKUdtJJrm6do_l0JPuafjo3P9mS4vHRCd.jpg?size=200x0&quality=96&crop=0,518,1620,1620&ava=1"
          }
        ]
      },
      {
        "person": {
          "id": 2503204,
          "firstName": "Sergey",
          "lastName": "Stepanov",
          "photoUri": "https://sun4-10.userapi.com/s/v1/ig2/nxhpqWcnIi-LuYLS_LZRs7V_-3MOMimJhwcvEhAyMoFmmO31MmvaaAjPEGk9BoFmuB0SLB4gnozhRJBycn3fQYRD.jpg?size=200x0&quality=96&crop=0,537,591,591&ava=1"
        },
        "hiddenFriends": [
          {
            "id": 103602,
            "firstName": "Евгений",
            "lastName": "Белов",
            "photoUri": "https://sun4-17.userapi.com/s/v1/if1/LHlKJ94vqH5sRHqVfC15PpLRXg8Drv2QexCOtlngav3eoyzzmh9_hbegIwOezh8j3b5ercOl.jpg?size=200x0&quality=96&crop=150,503,318,318&ava=1"
          }
        ]
      }
    ]
  }
  let people = []
  for (let entry of response.result) {
    people.push(<EntryAccordion key={entry.person.id} person={entry.person} friends={entry.hiddenFriends}/>)
  }
  return (
    <div style={styles}>{people}</div>
  );
}

export default Result;