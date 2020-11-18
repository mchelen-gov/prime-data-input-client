package gov.cdc.usds.simplereport.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import gov.cdc.usds.simplereport.api.model.Device;
import gov.cdc.usds.simplereport.api.model.Organization;
import gov.cdc.usds.simplereport.api.model.Patient;
import gov.cdc.usds.simplereport.api.model.TestOrder;
import gov.cdc.usds.simplereport.api.model.TestResult;
import gov.cdc.usds.simplereport.api.model.User;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Service;

@Service
public class DummyDataRepo {

	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	// set up or devices
	private Device device1 = new Device("Quidel Sofia 2","Quidel","Sofia 2");
	public ArrayList<Device> allDevices = new ArrayList<>(Arrays.asList(
		device1,
		new Device("BD Veritor","BD","Veritor"),
		new Device("Abbott Binax Now","Abbott","Binax Now"),
		new Device("Abbott IDNow","Abbott","IDNow"),
		new Device("LumiraDX","LumiraDX","")
	));
	public DataFetcher<List<Device>> deviceFetcher() {
		return (env) -> allDevices;
	}

	// set up default user and org
	public Organization defaultOrg = new Organization("Feel Good Inc", "clia1234" , "Gorillaz", "npi123", "123 abc st", "Apt 2", "Tuscon", "Pima County", "AZ", "54323", "123 456 7890", allDevices, device1);
	public User defaultUser = new User(defaultOrg);
	private ArrayList<Organization> allOrganizations = new ArrayList<>(Arrays.asList(defaultOrg));

	// add patients to org
	private Patient patient1 = new Patient("patientId1", "Edward", "", "Teach", LocalDate.of(1717, 1, 1), "123 Plank St", "", "Nassau", "NY", "12065", "(123) 456-7890", defaultOrg, "", "", "", "", "", "", false, false);
	private Patient patient3 = new Patient("patientId3", "John", "\"Long\"", "Silver", LocalDate.of(1729, 1, 1), "123 cat St", "", "lake view", "MI", "12067", "(213) 645-7890)", defaultOrg, "", "", "", "", "", "", false, false);

	// create test results
	private TestResult testResult1 = new TestResult(device1, "positive", patient1);
	private TestResult testResult2 = new TestResult(device1, "negative", patient1);
	private TestResult testResult3 = new TestResult(device1, "inconclusive", patient3);

	public ArrayList<Patient> allPatients = new ArrayList<>(Arrays.asList(
		patient1,
		new Patient("patientId2", "James", "D.", "Flint", LocalDate.of(1719, 1, 1), "123 dog St", "apt 2", "Jamestown", "VT", "12068", "(321) 546-7890", defaultOrg, "", "", "", "", "", "", false, false),
		patient3,
		new Patient("patientId4","Sally","Mae","Map", LocalDate.of(1922, 1, 1),"123 bird St", "", "mountain top", "VA", "12075","(243) 635-7190", defaultOrg, "", "", "", "", "", "", false, false),
		new Patient("patientId5","Apollo","Graph","QL", LocalDate.of(1901, 1, 1),"987 Plank St", "", "town name", "CA", "15065","(243) 555-5555", defaultOrg, "", "", "", "", "", "", false, false)
	));
	public DataFetcher<List<Patient>> patientFetcher() {
		return (env) -> allPatients;
	}

	public ArrayList<TestResult> allTestResults = new ArrayList<>(Arrays.asList(
		testResult1,
		testResult2,
		testResult3
	));

	public DataFetcher<List<TestResult>> testResultFetcher() {
		return (env) -> allTestResults;
	}

	public  DataFetcher<User> userFetcher() {
		return (env) -> defaultUser;
	}

	public  DataFetcher<Organization> organizationFetcher() {
		return (env) -> defaultOrg;
	}

	public ArrayList<TestOrder> queue = new ArrayList<>();

	public DataFetcher<List<TestOrder>> queueFetcher() {
		return (env) -> queue;
	}

	public String addPatient(
		String lookupId,
		String firstName,
		String middleName,
		String lastName,
		String birthDate,
		String street,
		String streetTwo,
		String city,
		String state,
		String zipCode,
		String phone,
		String typeOfHealthcareProfessional,
		String email,
		String county,
		String race,
		String ethnicity,
		String gender,
		Boolean residentCongregateSetting,
		Boolean employedInHealthcare
	) {
		LocalDate localBirthDateDate = (birthDate == null) ? null : LocalDate.parse(birthDate, this.dateTimeFormatter);

		Patient newPatient = new Patient(
			lookupId,
			firstName,
			middleName,
			lastName,
			localBirthDateDate,
			street,
			streetTwo,
			city,
			state,
			zipCode,
			phone,
			defaultOrg,
			typeOfHealthcareProfessional,
			email,
			county,
			race,
			ethnicity,
			gender,
			residentCongregateSetting,
			employedInHealthcare
		);
		allPatients.add(newPatient);
		return newPatient.getId();
	}

	public String updatePatient(
		String patientId,
		String lookupId,
		String firstName,
		String middleName,
		String lastName,
		String birthDate,
		String street,
		String streetTwo,
		String city,
		String state,
		String zipCode,
		String phone,
		String typeOfHealthcareProfessional,
		String email,
		String county,
		String race,
		String ethnicity,
		String gender,
		Boolean residentCongregateSetting,
		Boolean employedInHealthcare
	) {
		LocalDate localBirthDateDate = (birthDate == null) ? null : LocalDate.parse(birthDate, this.dateTimeFormatter);
		Patient patientToUpdate  = getPatient(patientId);
		patientToUpdate.updatePatient(
			lookupId,
			firstName,
			middleName,
			lastName,
			localBirthDateDate,
			street,
			streetTwo,
			city,
			state,
			zipCode,
			phone,
			defaultOrg,
			typeOfHealthcareProfessional,
			email,
			county,
			race,
			ethnicity,
			gender,
			residentCongregateSetting,
			employedInHealthcare
		);
		return patientToUpdate.getId();
	}

	public String updateOrganization(
		String testingFacilityName,
		String cliaNumber,
		String orderingProviderName,
		String orderingProviderNPI,
		String orderingProviderStreet,
		String orderingProviderStreetTwo,
		String orderingProviderCity,
		String orderingProviderCounty,
		String orderingProviderState,
		String orderingProviderZipCode,
		String orderingProviderPhone,
		List<String> deviceIds,
		String defaultDeviceId
	) {
		List<Device> newDevices = new ArrayList<Device>();
		deviceIds.forEach((id) -> {
			Device device = allDevices.stream().filter(d -> id.equals(d.getId())).findAny().orElse(null);
			if (device != null) {
				newDevices.add(device);
			}
		});

		defaultOrg.updateOrg(
			testingFacilityName,
			cliaNumber,
			orderingProviderName,
			orderingProviderNPI,
			orderingProviderStreet,
			orderingProviderStreetTwo,
			orderingProviderCity,
			orderingProviderCounty,
			orderingProviderState,
			orderingProviderZipCode,
			orderingProviderPhone,
			newDevices,
			this.getDevice(defaultDeviceId)
		);
		return defaultOrg.getId();
	}

	public Patient getPatient(String patientId) {
		for(Patient p : allPatients) {
			if(p.getId().equals(patientId)) {
				return p;
			}
		}
		return null;
	}

	Device getDevice(String deviceId) {
		for(Device d : allDevices) {
			if(d.getId().equals(deviceId)) {
				return d;
			}
		}
		return null;
	}

	public String addTestResult(
		String deviceId,
		String result,
		String patientId
	) {
		Patient patient = this.getPatient(patientId);
		TestResult newTestResult = new TestResult(
			this.getDevice(deviceId),
			result,
			patient
		);
		patient.addTestResult(newTestResult);
		allTestResults.add(newTestResult);
		return newTestResult.getId();
	}

	public String addPatientToQueue(
		String patientId,
		String pregnancy,
		String symptoms,
		Boolean firstTest,
		String priorTestDate,
		String priorTestType,
		String priorTestResult,
		String symptomOnset,
		Boolean noSymptoms
	) {
		Patient patient = this.getPatient(patientId);
		TestOrder newTestOrder = new TestOrder(
			patient,
			defaultOrg
		);
		queue.add(newTestOrder);
		LocalDate localSymptomOnset = (symptomOnset == null) ? null : LocalDate.parse(symptomOnset, this.dateTimeFormatter);
		LocalDate localPriorTestDate = (priorTestDate == null) ? null : LocalDate.parse(priorTestDate, this.dateTimeFormatter);

		newTestOrder.setSurveyResponses(
			pregnancy,
			symptoms,
			firstTest,
			localPriorTestDate,
			priorTestType,
			priorTestResult,
			localSymptomOnset,
			noSymptoms
		);
		newTestOrder.setDevice(defaultOrg.getDefaultDevice());
		return newTestOrder.getId();
	}

	public int getQueueIndexByPatientId(String patientId) {
		int index = 0;
		while(index < queue.size()) {
			  TestOrder order = queue.get(index);
				if(order.getPatientId().equals(patientId)) {
					return index;
				}
				index++;
		} 
		return -1;
	}

	public String removePatientFromQueue(String patientId) {
		int index = this.getQueueIndexByPatientId(patientId);
		queue.remove(index);
		return "";
	}

	public String updateTimeOfTestQuestions(
		String patientId,
		String pregnancy,
		String symptoms,
		Boolean firstTest,
		String priorTestDate,
		String priorTestType,
		String priorTestResult,
		String symptomOnset,
		Boolean noSymptoms
	) {
		TestOrder testOrder = queue.get(this.getQueueIndexByPatientId(patientId));
		LocalDate localSymptomOnset = (symptomOnset == null) ? null : LocalDate.parse(symptomOnset, this.dateTimeFormatter);
		LocalDate localPriorTestDate = (priorTestDate == null) ? null : LocalDate.parse(priorTestDate, this.dateTimeFormatter);

		testOrder.setSurveyResponses(
			pregnancy,
			symptoms,
			firstTest,
			localPriorTestDate,
			priorTestType,
			priorTestResult,
			localSymptomOnset,
			noSymptoms
		);
		return "";
	}

	public String updateDeviceForPatientInQueue(String patientID, String deviceId) {
		TestOrder testOrder = queue.get(this.getQueueIndexByPatientId(patientID));
		testOrder.setDevice(this.getDevice(deviceId));
		return "";
	}

	public String updateResultForPatientInQueue(String patientID, String result) {
		TestOrder testOrder = queue.get(this.getQueueIndexByPatientId(patientID));
		testOrder.setTestResult(result);
		return "";
	}

	public void init_relations() {
		patient1.setTestResults(new ArrayList<>(Arrays.asList(testResult1, testResult2)));
		patient3.setTestResults(new ArrayList<>(Arrays.asList(testResult3)));
	}
}
