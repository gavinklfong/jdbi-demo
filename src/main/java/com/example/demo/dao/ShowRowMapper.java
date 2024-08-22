package com.example.demo.dao;

import com.example.demo.model.TheatreShow;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

@Component
public class ShowRowMapper implements RowMapper<TheatreShow> {
    @Override
    public TheatreShow map(ResultSet rs, StatementContext ctx) throws SQLException {
        return TheatreShow.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .duration(Duration.ofMinutes(rs.getInt("duration_in_mins")))
                .location(rs.getString("venue"))
                .startTime(rs.getTimestamp("start_time").toLocalDateTime())
                .build();
    }
}
