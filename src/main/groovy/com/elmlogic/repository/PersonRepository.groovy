package com.elmlogic.repository

import com.elmlogic.domain.Person
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository

@Repository
public interface PersonRepository extends GraphRepository<Person> {

    Person findByName(String name);

    List<Person> findByTeammatesName(String name);

}
