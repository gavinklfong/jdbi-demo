package com.example.demo.dao;

import com.example.demo.model.Show;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TheatreShowDao {

    private static final String SEARCH_SHOW_BY_NAME = "SELECT id, name, venue, start_time, duration_in_mins " +
            "FROM theatre_show " +
            "WHERE name LIKE :name " +
            "ORDER BY start_time asc";

    private final Jdbi jdbi;

    public List<Show> searchShowByName(String name) {
        return jdbi.withHandle(handle ->
            handle.createQuery(SEARCH_SHOW_BY_NAME)
                    .bind("name", name + "%")
                    .map(new ShowRowMapper())
                    .collectIntoList()
        );
    }


}
