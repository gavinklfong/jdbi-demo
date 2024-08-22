package com.example.demo.util;

import com.example.demo.dao.ReservationRowMapper;
import com.example.demo.dao.SeatRowMapper;
import com.example.demo.model.BookingStatus;
import com.example.demo.model.Reservation;
import com.example.demo.model.Seat;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class DbTestUtil {

    private static final String GET_RESERVATION_BY_ID = "SELECT id, customer_id, total_price, reservation_time " +
            "FROM reservation WHERE id = :id";

    private static final String GET_RESERVATION_BY_CUSTOMER_ID = "SELECT id, customer_id, total_price, reservation_time " +
            "FROM reservation WHERE customer_id = :customerId";

    private static final String GET_SEATS_BY_ID = "SELECT show_id, seat_id, region, price, status, reservation_id " +
            "FROM seat " +
            "WHERE show_id = :showId AND seat_id IN (<seatIds>)";

    public static void verifyReservationNotCreated(Jdbi jdbi, String customerId) {
        Optional<Reservation> actualReservation = jdbi.withHandle(h ->
                h.createQuery(GET_RESERVATION_BY_CUSTOMER_ID)
                        .bind("customerId", customerId)
                        .map(new ReservationRowMapper())
                        .findOne()
        );
        assertThat(actualReservation).isEmpty();
    }

    public static void verifyReservationNotCreated(Jdbi jdbi, Reservation reservation) {
        Optional<Reservation> actualReservation = jdbi.withHandle(h ->
                h.createQuery(GET_RESERVATION_BY_ID)
                        .bind("id", reservation.getId())
                        .map(new ReservationRowMapper())
                        .findOne()
        );
        assertThat(actualReservation).isEmpty();
    }

    public static void verifySeatsNotReserved(Jdbi jdbi, String showId, Set<String> seatIds, String reservationId) {
        Set<Seat> updatedSeats = jdbi.withHandle(h ->
                h.createQuery(GET_SEATS_BY_ID)
                        .bind("showId", showId)
                        .bindList("seatIds", seatIds)
                        .map(new SeatRowMapper())
                        .set()
        );

        assertThat(updatedSeats).hasSize(seatIds.size())
                .filteredOn("reservationRef", in(reservationId))
                .filteredOn("status", in(BookingStatus.RESERVED))
                .isEmpty();
    }

    public static void verifySeatNotReservedWithExpectedStatus(Jdbi jdbi, String showId, String seatId, BookingStatus expectedStatus, boolean nullReservationId) {
        Optional<Seat> seatOptional = jdbi.withHandle(h ->
                h.createQuery(GET_SEATS_BY_ID)
                        .bind("showId", showId)
                        .bindList("seatIds", List.of(seatId))
                        .map(new SeatRowMapper())
                        .findOne()
        );

        assertThat(seatOptional).isNotEmpty();
        Seat seat = seatOptional.get();
        if (nullReservationId) {
            assertThat(seat.getReservationRef()).isNull();
        } else {
            assertThat(seat.getReservationRef()).isNotNull();
        }

        assertThat(seat.getStatus()).isEqualTo(expectedStatus);
    }

    public static void verifySeatsHaveBeenReserved(Jdbi jdbi, String showId, Set<String> seatIds, String reservationId) {
        Set<Seat> updatedSeats = jdbi.withHandle(h ->
                h.createQuery(GET_SEATS_BY_ID)
                        .bind("showId", showId)
                        .bindList("seatIds", seatIds)
                        .map(new SeatRowMapper())
                        .set()
        );

        assertThat(updatedSeats).hasSize(seatIds.size())
                .extracting("status", "reservationRef")
                .contains(tuple(BookingStatus.RESERVED, reservationId));
    }

    public static void verifyReservationCreated(Jdbi jdbi, Reservation reservation) {
        Optional<Reservation> actualReservation = jdbi.withHandle(h ->
                h.createQuery(GET_RESERVATION_BY_ID)
                        .bind("id", reservation.getId())
                        .map(new ReservationRowMapper())
                        .findOne()
        );
        assertThat(actualReservation).isNotEmpty();
    }

    public static void verifyReservationCreated(Jdbi jdbi, String customerId) {
        Optional<Reservation> actualReservation = jdbi.withHandle(h ->
                h.createQuery(GET_RESERVATION_BY_CUSTOMER_ID)
                        .bind("customerId", customerId)
                        .map(new ReservationRowMapper())
                        .findOne()
        );
        assertThat(actualReservation).isNotEmpty();
    }
}
