package gov.cdc.usds.simplereport.db.repository;

import gov.cdc.usds.simplereport.db.model.Organization;
import gov.cdc.usds.simplereport.db.model.Person;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Interface specification for fetching and manipulating {@link Person} entities
 */
public interface PersonRepository extends EternalEntityRepository<Person> {

    @Query(BASE_QUERY + " and organization = :org")
    public List<Person> findAllByOrganization(Organization org);

    @Query(BASE_QUERY + " and internalId = :id and organization = :org")
    public Person findByIDAndOrganization(UUID id, Organization org);
}
