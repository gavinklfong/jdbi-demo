package com.example.demo.dao;

import com.example.demo.model.Reservation;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReservationRowMapper implements RowMapper<Reservation> {
    @Override
    public Reservation map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Reservation.builder()
                .id(rs.getString("id"))
                .customerId(rs.getString("customer_id"))
                .totalPrice(rs.getBigDecimal("total_price"))
                .reservationTime(rs.getTimestamp("reservation_time").toLocalDateTime())
                .build();
    }
}
