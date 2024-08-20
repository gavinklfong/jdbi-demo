package com.example.demo.dao;

import com.example.demo.model.Show;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

public class ShowRowMapper implements RowMapper<Show> {
    @Override
    public Show map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Show.builder()
                .id(rs.getString("id"))
                .duration(Duration.ofMinutes(rs.getInt("duration_in_mins")))
                .location(rs.getString("venue"))
                .startTime(rs.getTimestamp("start_time").toLocalDateTime())
                .build();
    }
}
