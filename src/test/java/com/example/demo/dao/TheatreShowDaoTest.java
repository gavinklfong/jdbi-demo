package com.example.demo.dao;


import org.jdbi.v3.testing.junit5.tc.JdbiTestcontainersExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.jdbi.v3.testing.junit5.JdbiExtension;

@Testcontainers
public class TheatreShowDaoTest {

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer();

    @RegisterExtension
    private static final JdbiExtension extension = JdbiTestcontainersExtension.instance(MY_SQL_CONTAINER);



}
