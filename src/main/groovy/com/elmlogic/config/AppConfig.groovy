package com.elmlogic.config

import com.elmlogic.TestDriver
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.kernel.impl.util.FileUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.aspectj.EnableSpringConfigured
import org.springframework.data.neo4j.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.config.Neo4jConfiguration
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories("com.elmlogic")
public class AppConfig extends Neo4jConfiguration {
    AppConfig() {
    setBasePackage("com.elmlogic");
    }
    @Bean
    public GraphDatabaseService graphDatabaseService() {
        // if you want to use Neo4j as a REST service
        //return new SpringRestGraphDatabase("http://localhost:7474/db/data/");
        // Use Neo4j as Odin intended (as an embedded service)
        FileUtils.deleteRecursively(new File("/tmp/graphdb"));
        GraphDatabaseService service = new GraphDatabaseFactory().newEmbeddedDatabase("/tmp/graphdb");
        return service;
    }

    @Bean
    TestDriver testerDriver() {
        return new TestDriver()
    }
}