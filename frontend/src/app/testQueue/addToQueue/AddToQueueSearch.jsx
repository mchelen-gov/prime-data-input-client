import React, { useState } from "react";
import { toast } from "react-toastify";
import { gql, useQuery, useMutation } from "@apollo/client";
import Alert from "../../commonComponents/Alert";
import SearchInput from "./SearchInput";
import SearchResults from "./SearchResults";
import { QUEUE_NOTIFICATION_TYPES, ALERT_CONTENT } from "../constants";
import { showNotification } from "../../utils";
import { displayFullName } from "../../utils";

const MIN_SEARCH_CHARACTER_COUNT = 3;

const QUERY_PATIENTS_AND_QUEUE = gql`
  {
    patients {
      internalId
      lookupId
      firstName
      lastName
      middleName
      birthDate
    }
    queue {
      patient {
        internalId
      }
    }
  }
`;

const ADD_PATIENT_TO_QUEUE = gql`
  mutation(
    $patientId: String!
    $symptoms: String
    $symptomOnset: String
    $pregnancy: String
    $firstTest: Boolean
    $priorTestDate: String
    $priorTestType: String
    $priorTestResult: String
    $noSymptoms: Boolean
  ) {
    addPatientToQueue(
      patientId: $patientId
      pregnancy: $pregnancy
      noSymptoms: $noSymptoms
      symptoms: $symptoms
      firstTest: $firstTest
      priorTestDate: $priorTestDate
      priorTestType: $priorTestType
      priorTestResult: $priorTestResult
      symptomOnset: $symptomOnset
    )
  }
`;

const AddToQueueSearchBox = ({ refetchQueue }) => {
  const { data, loading, error } = useQuery(QUERY_PATIENTS_AND_QUEUE, {
    fetchPolicy: "cache-and-network",
  });
  if (loading) {
    console.log("loading patient data for search");
  }
  if (error) {
    console.error("Error loading patient data for search");
  }
  const [addPatientToQueue] = useMutation(ADD_PATIENT_TO_QUEUE);

  const [queryString, setQueryString] = useState("");
  const [suggestions, updateSuggestions] = useState([]);
  let shouldShowSuggestions = queryString.length >= MIN_SEARCH_CHARACTER_COUNT;

  const getSuggestionsFromQueryString = (queryString) => {
    if (data && data.patients) {
      let formattedQueryString = queryString.toLowerCase();
      let searchResults = data.patients.filter((patient) => {
        let doesMatchPatientName =
          displayFullName(
            patient.firstName,
            patient.middleName,
            patient.lastName
          )
            .toLowerCase()
            .indexOf(formattedQueryString) > -1;

        let doesMatchLookupId = patient.lookupId
          ? patient.lookupId.toLowerCase().indexOf(formattedQueryString) > -1
          : false;
        return doesMatchPatientName || doesMatchLookupId;
      });
      // See if any of these are in the queue
      const inQueue = {};
      data.queue.forEach((q) => (inQueue[q.patientId] = true));
      searchResults.map((r) => ({ ...r, isInQueue: inQueue[r.patientId] }));
      return searchResults;
    }
    return [];
  };

  const onInputChange = (event) => {
    let newValue = event.target.value;
    setQueryString(newValue);
    if (newValue.length > 2) {
      updateSuggestions(getSuggestionsFromQueryString(newValue));
    }
  };

  const onSearchClick = (event) => {
    event.preventDefault();
    updateSuggestions(getSuggestionsFromQueryString(queryString));
  };

  const onAddToQueue = (
    patient,
    {
      noSymptoms,
      symptoms,
      symptomOnset,
      pregnancy,
      firstTest,
      priorTestResult,
      priorTestDate,
      priorTestType,
    }
  ) => {
    updateSuggestions([]);
    setQueryString("");
    addPatientToQueue({
      variables: {
        patientId: patient.internalId,
        noSymptoms,
        symptoms,
        symptomOnset,
        pregnancy,
        firstTest,
        priorTestDate,
        priorTestType,
        priorTestResult,
      },
    }).then(
      (res) => {
        let { type, title, body } = {
          ...ALERT_CONTENT[QUEUE_NOTIFICATION_TYPES.ADDED_TO_QUEUE__SUCCESS](
            patient
          ),
        };
        let alert = <Alert type={type} title={title} body={body} />;
        showNotification(toast, alert);
        refetchQueue();
      },
      (err) => console.error(err)
    );
  };

  return (
    <React.Fragment>
      <SearchInput
        onSearchClick={onSearchClick}
        onInputChange={onInputChange}
        queryString={queryString}
        disabled={!shouldShowSuggestions}
      />
      <SearchResults
        suggestions={suggestions}
        shouldDisplay={shouldShowSuggestions}
        onAddToQueue={onAddToQueue}
      />
    </React.Fragment>
  );
};

export default AddToQueueSearchBox;
