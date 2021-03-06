package gov.cdc.usds.simplereport.db.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import gov.cdc.usds.simplereport.db.model.Organization;
import gov.cdc.usds.simplereport.db.model.Person;
import gov.cdc.usds.simplereport.db.model.TestEvent;
import gov.cdc.usds.simplereport.db.model.auxiliary.TestResult;
import gov.cdc.usds.simplereport.test_util.TestDataFactory;

public class TestEventRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private TestEventRepository _repo;
	@Autowired
	private TestDataFactory _dataFactory;

	@Test
	public void testFindByPatient() {
		Organization org = _dataFactory.createValidOrg();
		Person patient = _dataFactory.createMinimalPerson(org);
		_repo.save(new TestEvent(TestResult.POSITIVE, org.getDefaultDeviceType(), patient, org));
		_repo.save(new TestEvent(TestResult.UNDETERMINED, org.getDefaultDeviceType(), patient, org));
		flush();
		List<TestEvent> found = _repo.findAllByPatient(patient);
		assertEquals(2, found.size());
	}
}
