import React, { useEffect } from "react";
import moment from "moment";
import Modal from "react-modal";
import { v4 as uuidv4 } from "uuid";

import Alert from "../commonComponents/Alert";
import Button from "../commonComponents/Button";
import { displayFullName } from "../utils";

Modal.setAppElement("#root");

const CSVModalForm = ({ isOpen, onClose, data }) => {
  useEffect(() => {
    Modal.setAppElement("#root");
  });

  const PreviewTable = (data) => {
    if (!data) {
      return null;
    }

    // there are lots of rows -- do we render them all?
    let rows = data.map((row) => {
      const {
        patientID,
        patientLastName,
        patientFirstName,
        patientMiddleName,
        patientSuffix,
        patientRace,
        patientDOB,
        patientGender,
        patientEthnicity,
        patientStreet,
        patientStreet2,
        patientCity,
        patientCounty,
        patientState,
        patientZipCode,
        patientPhoneNumber,
        patientEmail,
        patientAge,
        employedInHealthcare,
        role,
        residentCongregateSetting,
        patientResidencyType,
      } = { ...row };

      return (
        <tr key={`patient-${uuidv4()}`}>
          <td>{patientSuffix}</td>
          <th scope="row">
            {displayFullName(
              patientFirstName,
              patientMiddleName,
              patientLastName
            )}
          </th>
          <td>{patientID}</td>
          <td>{moment(patientDOB).format("MMM DD YYYY")}</td>
          <td>{patientGender}</td>
          <td>{patientEthnicity}</td>
          <td>{patientRace}</td>
          <td>{patientAge}</td>
          <td>{patientPhoneNumber}</td>
          <td>{patientEmail}</td>
          <td>{patientStreet}</td>
          <td>{patientStreet2}</td>
          <td>{patientCity}</td>
          <td>{patientCounty}</td>
          <td>{patientState}</td>
          <td>{patientZipCode}</td>
          <td>{employedInHealthcare}</td>
          <td>{role}</td>
          <td>{residentCongregateSetting}</td>
          <td>{patientResidencyType}</td>
        </tr>
      );
    });

    return (
      <div className="prime-csv-import-columns">
        <table className="usa-table usa-table--borderless width-full">
          <thead>
            <tr>
              <th scope="col">Suffix</th>
              <th scope="col">Name</th>
              <th scope="col">Unique ID</th>
              <th scope="col">Date of Birth</th>
              <th scope="col">Gender</th>
              <th scope="col">Ethnicity</th>
              <th scope="col">Race</th>
              <th scope="col">Age</th>
              <th scope="col">Phone</th>
              <th scope="col">Email</th>
              <th scope="col">Street</th>
              <th scope="col">Street 2</th>
              <th scope="col">City</th>
              <th scope="col">County</th>
              <th scope="col">State</th>
              <th scope="col">Zip Code</th>
              <th scope="col">Employed in Healthcare</th>
              <th scope="col">Type of Healthcare Professional</th>
              <th scope="col">Resident Congregate Setting</th>
              <th scope="col">Residency Type</th>
            </tr>
          </thead>
          <tbody>{rows}</tbody>
        </table>
      </div>
    );
  };

  const ErrorTable = (data) => {
    if (!data) {
      return null;
    }
    let rows = data.map((errorRow) => {
      const { message, row } = { ...errorRow };
      const { patientID, patientLastName, patientFirstName } = {
        ...errorRow.original,
      };

      return (
        <tr key={`patient-${uuidv4()}`}>
          <th scope="row">
            {displayFullName(patientFirstName, "", patientLastName)}
          </th>
          <td>{patientID}</td>
          <td>{row}</td>
          <td>{message}</td>
        </tr>
      );
    });

    // alerts styles are currently broken
    const alert = (
      <Alert
        type="warning"
        title="The following records will not be added due to exact matches found in the system"
        body={`${data.length} records will not be included in the upload.`}
      />
    );

    return (
      <React.Fragment>
        {alert}
        <div className="prime-csv-import-columns">
          <table className="usa-table usa-table--borderless width-full">
            <thead>
              <tr>
                <th scope="col">Name</th>
                <th scope="col">Unique ID</th>
                <th scope="col">Row #</th>
                <th scope="col">Message</th>
              </tr>
            </thead>
            <tbody>{rows}</tbody>
          </table>
        </div>
      </React.Fragment>
    );
  };

  return (
    <Modal
      isOpen={isOpen}
      style={{ overlay: { zIndex: 1000 } }}
      overlayClassName={"prime-modal-overlay"}
      // className={"prime-modal"}
      contentLabel="Example Modal"
    >
      <div className="grid-container">
        <div className="grid-row">
          <h1>CSV Import</h1>
        </div>
        <div className="grid-row">
          <Button type="button" onClick={onClose} label="Cancel" />
          <Button type="button" onClick={() => {}} label="Confirm Import" />
        </div>
        <div>
          <h2>Preview:</h2>
          {PreviewTable(data.data)}
        </div>
        <div>
          <h2>Bad Rows:</h2>
          {ErrorTable(data.badRows)}
        </div>
        <div className="grid-row">
          <Button type="button" onClick={onClose} label="Cancel" />
          <Button type="button" onClick={() => {}} label="Confirm Import" />
        </div>
      </div>
    </Modal>
  );
};

CSVModalForm.propTypes = {};

export default CSVModalForm;
