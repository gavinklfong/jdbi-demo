package com.example.demo.dao;

import com.example.demo.model.Show;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TheatreShowDao {

    private static final String SEARCH_SHOW_BY_NAME = "SELECT id, name, location, start_time, duration " +
            "FROM theatre_show " +
            "WHERE name LIKE ':name%'";

    private final Jdbi jdbi;

    public Set<Show> searchShowByName(String name) {
        return jdbi.withHandle(handle ->
            handle.createQuery(SEARCH_SHOW_BY_NAME)
                    .bind("name", name)
                    .mapToBean(Show.class)
                    .collectIntoSet()
        );
    }


}
