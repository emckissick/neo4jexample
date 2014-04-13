package com.elmlogic

import com.elmlogic.config.AppConfig
import com.elmlogic.domain.Person
import com.elmlogic.repository.PersonRepository
import org.neo4j.graphdb.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.neo4j.core.GraphDatabase
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class TestDriver {
    @Resource
    PersonRepository personRepository

    @Autowired
    GraphDatabase graphDatabase
    static void main(String[] args) {

       ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        TestDriver driver = ctx.getBean(TestDriver.class)
        driver.someTest()


    }

    void someTest() {
        Person greg = new Person("Greg");
        Person roy = new Person("Roy");
        Person craig = new Person("Craig");

        System.out.println("Before linking up with Neo4j...");
        for (Person person : [greg, roy, craig]) {
            System.out.println(person);
        }

        Transaction tx = graphDatabase.beginTx();
        try {
            personRepository.save(greg);
            personRepository.save(roy);
            personRepository.save(craig);

            greg = personRepository.findByName(greg.name);
            greg.worksWith(roy);
            greg.worksWith(craig);
            personRepository.save(greg);

            roy = personRepository.findByName(roy.name);
            roy.worksWith(craig);
            // We already know that roy works with greg
            personRepository.save(roy);

            // We already know craig works with roy and greg

            tx.success();
        } finally {
            tx.finish();
        }

        System.out.println("Lookup each person by name...");
        for (String name: [greg.name, roy.name, craig.name]) {
            System.out.println(personRepository.findByName(name));
        }

        System.out.println("Looking up who works with Greg...");
        List<Person> teamMates = personRepository.findByTeammatesName('Greg')
        teamMates.each {
            println(it.name + " works with Greg.");
        }
    }
}
