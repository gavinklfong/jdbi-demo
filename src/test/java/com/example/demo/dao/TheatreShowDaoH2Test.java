package com.example.demo.dao;


import com.example.demo.model.Show;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.JdbiH2Extension;
import org.jdbi.v3.testing.junit5.tc.JdbiTestcontainersExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

@Slf4j
@ExtendWith(JdbiH2Extension.class)
class TheatreShowDaoH2Test {

    private TheatreShowDao theatreShowDao;

    @BeforeEach
    void setup(Jdbi jdbi) {
        jdbi.useHandle(h -> {
            h.getConfig(SqlStatements.class).setScriptStatementsNeedSemicolon(false);
            h.createScript(ClasspathSqlLocator.removingComments()
                            .getResource("db-scripts/theatre_show_schema.sql"))
                    .execute();
            h.createScript(ClasspathSqlLocator.removingComments()
                            .getResource("db-scripts/theatre_show_test_data.sql"))
                    .execute();
        });

        theatreShowDao = new TheatreShowDao(jdbi);
    }

    @Test
    void queryTest() {
        Set<Show> results = theatreShowDao.searchShowByName("The Lion King");

    }

}
