package com.example.demo.dao;

import com.example.demo.model.BookingStatus;
import com.example.demo.model.Seat;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SeatRowMapper implements RowMapper<Seat> {
    @Override
    public Seat map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Seat.builder()
                .showId(rs.getString("show_id"))
                .seatId(rs.getString("seat_id"))
                .region(rs.getString("region"))
                .price(rs.getBigDecimal("price"))
                .status(BookingStatus.valueOf(rs.getString("status")))
                .reservationRef(rs.getString("reservation_id"))
                .build();
    }
}
