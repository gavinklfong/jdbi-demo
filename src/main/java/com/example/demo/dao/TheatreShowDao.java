package com.example.demo.dao;

import com.example.demo.model.Reservation;
import com.example.demo.model.Show;
import com.example.demo.model.Seat;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class TheatreShowDao {

    private static final String SEARCH_SHOW_BY_NAME = "SELECT id, name, venue, start_time, duration_in_mins " +
            "FROM theatre_show " +
            "WHERE name LIKE :name " +
            "ORDER BY start_time asc";

    private static final String GET_SHOW_BY_ID = "SELECT id, name, venue, start_time, duration_in_mins " +
            "FROM theatre_show " +
            "WHERE id = :showId";

    private static final String GET_SEATS_BY_SHOW = "SELECT show_id, seat_id, region, status, price, reservation_id " +
            "FROM seat " +
            "WHERE show_id = :showId " +
            "ORDER BY seat_id asc";

    private static final String GET_SEATS_BY_ID = "SELECT show_id, seat_id, region, status, price, reservation_id " +
            "FROM seat " +
            "WHERE show_id = :showId " +
            "AND seat_id IN (<seatIds>)" +
            "ORDER BY seat_id asc";

    private static final String RESERVE_SEATS = "UPDATE seat SET " +
            "status = 'RESERVED'," +
            "reservation_id = :reservationId " +
            "WHERE show_id = :showId " +
            "AND status = 'AVAILABLE' " +
            "AND seat_id in (<seatIds>)";

    private static final String INSERT_RESERVATION = "INSERT INTO reservation (id, customer_id, total_price, reservation_time) VALUES (" +
            ":id, :customerId, :totalPrice, :reservationTime)";

    private final Jdbi jdbi;
    private final SeatRowMapper seatRowMapper;
    private final ShowRowMapper showRowMapper;
    private final ReservationRowMapper reservationRowMapper;

    public List<Show> searchShowByName(String name) {
        return jdbi.withHandle(handle ->
            handle.createQuery(SEARCH_SHOW_BY_NAME)
                    .bind("name", name + "%")
                    .map(showRowMapper)
                    .list()
        );
    }

    public Optional<Show> getShowById(String showId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(GET_SHOW_BY_ID)
                        .bind("showId", showId)
                        .map(showRowMapper)
                        .findOne()
        );
    }

    public List<Seat> getSeatsByShowId(String showId) {
        return jdbi.withHandle(handle ->
                handle.createQuery(GET_SEATS_BY_SHOW)
                        .bind("showId", showId)
                        .map(seatRowMapper)
                        .list()
        );
    }


    public List<Seat> getSeatsByShowIdAndSeatId(String showId, Set<String> seatIds) {
        return jdbi.withHandle(handle ->
                handle.createQuery(GET_SEATS_BY_ID)
                        .bind("showId", showId)
                        .bindList("seatIds", seatIds)
                        .map(seatRowMapper)
                        .list()
        );
    }

    public int reserveSeats(String showId, Set<String> seatIds, String reservationId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(RESERVE_SEATS)
                        .bind("showId", showId)
                        .bind("reservationId", reservationId)
                        .bindList("seatIds", seatIds)
                        .execute()
        );
    }

    public int insertReservation(Reservation reservation) {
        return jdbi.withHandle(handle ->
                handle.createUpdate(INSERT_RESERVATION)
                        .bindBean(reservation)
                        .execute()
        );
    }

    public void createReservationAndReserveSeats(String showId, Set<String> seatIds, Reservation reservation) {
        jdbi.useTransaction(handle -> {
            int reservationCount = handle.createUpdate(INSERT_RESERVATION)
                    .bindBean(reservation)
                    .execute();

            if (reservationCount != 1) {
                throw new RuntimeException("fail to create reservation");
            }

            int reservedSeatCount = handle.createUpdate(RESERVE_SEATS)
                    .bind("showId", showId)
                    .bind("reservationId", reservation.getId())
                    .bindList("seatIds", seatIds)
                    .execute();

            if (reservedSeatCount < seatIds.size()) {
                throw new RuntimeException("fail to reserve seat");
            }
        });

    }

}
