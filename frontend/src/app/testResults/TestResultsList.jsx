import { gql, useQuery } from "@apollo/client";
import React from "react";
import moment from "moment";

import { PATIENT_TERM_CAP } from "../../config/constants";
import { displayFullName } from "../utils";

const testResultQuery = gql`
  {
    testResults {
      internalId
      dateTested
      result
      deviceType {
        internalId
        name
      }
      patient {
        internalId
        firstName
        middleName
        lastName
        lookupId
      }
    }
  }
`;

const TestResultsList = () => {
  const { data, loading, error } = useQuery(testResultQuery, {
    fetchPolicy: "cache-and-network",
  });

  if (loading) {
    return <p>Loading</p>;
  }
  if (error) {
    console.error(error);
    return (
      <div>
        <h3>There was an error:</h3>
      </div>
    );
  }

  const testResultRows = (testResults) => {
    if (testResults.length === 0) {
      return;
    }
    return testResults.map((result) => {
      // TODO: patientId is the lookup id. This needs to be renamed
      const { firstName, middleName, lastName, lookupId } = {
        ...result.patient,
      };

      return (
        <tr key={result.internalId}>
          <th scope="row">
            {displayFullName(firstName, middleName, lastName)}
          </th>
          <td>{lookupId}</td>
          <td>{moment(result.dateTested).format("MMM DD YYYY")}</td>
          <td>{result.result}</td>
          <td>{result.deviceType.name}</td>
        </tr>
      );
    });
  };

  let rows = testResultRows(data.testResults);
  return (
    <React.Fragment>
      <main className="prime-home">
        <div className="grid-container">
          <div className="grid-row">
            <div className="prime-container usa-card__container">
              <div className="usa-card__header">
                <h2> Test Results </h2>
              </div>
              <div className="usa-card__body">
                <table className="usa-table usa-table--borderless width-full">
                  <thead>
                    <tr>
                      <th scope="col">{PATIENT_TERM_CAP} Name</th>
                      <th scope="col">Unique ID</th>
                      <th scope="col">Date of Test</th>
                      <th scope="col">Result</th>
                      <th scope="col">Device</th>
                    </tr>
                  </thead>
                  <tbody>{rows}</tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </main>
    </React.Fragment>
  );
};

export default TestResultsList;
