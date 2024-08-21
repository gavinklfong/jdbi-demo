package com.example.demo.dao;


import com.example.demo.model.BookingStatus;
import com.example.demo.model.Reservation;
import com.example.demo.model.Seat;
import com.example.demo.model.Show;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.testing.junit5.JdbiH2Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@ExtendWith(JdbiH2Extension.class)
class TheatreShowDaoH2Test {

    private static final String GET_RESERVATION_BY_ID = "SELECT id, customer_id, total_price, reservation_time " +
            "FROM reservation WHERE id = :id";
    private static final String GET_SEATS_BY_ID = "SELECT show_id, seat_id, region, price, status, reservation_id " +
            "FROM seat " +
            "WHERE show_id = :showId AND seat_id IN (<seatIds>)";

    private static final String SHOW_ID = "1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8";
    private static final String NEW_RESERVATION_ID = "6e1becbd-6acf-453f-a37e-1d2b5a17800b";
    private static final String CUSTOMER_ID = "5e9d267a-84cb-4317-bf75-1ce69d425455";

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

        theatreShowDao = new TheatreShowDao(jdbi, new SeatRowMapper(), new ShowRowMapper(), new ReservationRowMapper());
    }

    @Test
    void givenShowExist_testSearchShowByName() {
        List<Show> results = theatreShowDao.searchShowByName("The Lion King");
        assertThat(results).hasSize(3)
                .extracting("name")
                .contains("The Lion King");
    }

    @Test
    void givenShowExist_testGetShowById() {
        Optional<Show> result = theatreShowDao.getShowById(SHOW_ID);
        assertThat(result).isNotEmpty()
                .contains(Show.builder()
                        .id(SHOW_ID)
                        .name("Wicked")
                        .location("Apollo Victoria Theatre")
                        .duration(Duration.ofMinutes(165))
                        .startTime(LocalDateTime.parse("2024-08-21T14:30:00"))
                        .build());
    }

    @Test
    void givenShowNotExist_testGetShowById() {
        Optional<Show> result = theatreShowDao.getShowById("non-existing-show");
        assertThat(result).isEmpty();
    }

    @Test
    void givenShowExist_testGetSeatByShowId() {
        List<Seat> seats = theatreShowDao.getSeatsByShowId(SHOW_ID);
        assertThat(seats).hasSize(10);
    }

    @Test
    void testGetSeatsByShowIdAndSeatId() {
        Set<String> seatIds = Set.of("ZF44", "ZF45");
        List<Seat> seats = theatreShowDao.getSeatsByShowIdAndSeatId(SHOW_ID, seatIds);
        assertThat(seats).hasSize(seatIds.size());
    }

    @Test
    void testInsertReservation(Jdbi jdbi) {
        Reservation reservation = buildReservation();
        int count = theatreShowDao.insertReservation(reservation);
        assertThat(count).isEqualTo(1);

        verifyReservationCreated(jdbi, reservation);
    }

    @Test
    void givenAvailableSeats_testReserveSeats(Jdbi jdbi) {
        // insert reservation
        Reservation reservation = buildReservation();
        theatreShowDao.insertReservation(reservation);

        // reserve seats
        Set<String> seatIds = Set.of("ZF44", "ZF45");
        int updateCount = theatreShowDao.reserveSeats(SHOW_ID, seatIds, NEW_RESERVATION_ID);
        assertThat(updateCount).isEqualTo(seatIds.size());

        // verify reserved seats
        verifySeatsHaveBeenReserved(jdbi, SHOW_ID, seatIds, NEW_RESERVATION_ID);
    }

    @Test
    void givenSomeSeatNotAvailable_testReserveSeats() {
        // insert reservation
        Reservation reservation = buildReservation();
        theatreShowDao.insertReservation(reservation);

        // reserve seats
        Set<String> seatIds = Set.of("ZE3", "ZE4");
        int updateCount = theatreShowDao.reserveSeats(SHOW_ID, seatIds, NEW_RESERVATION_ID);
        assertThat(updateCount).isEqualTo(1);
    }

    @Test
    void givenSeatAvailable_createReservationAndReserveSeats(Jdbi jdbi) {

        Reservation reservation = buildReservation();

        Set<String> seatIds = Set.of("ZF44", "ZF45");
        theatreShowDao.createReservationAndReserveSeats(SHOW_ID, seatIds, reservation);

        verifyReservationCreated(jdbi, reservation);
        verifySeatsHaveBeenReserved(jdbi, SHOW_ID, seatIds, NEW_RESERVATION_ID);
    }

    @Test
    void givenSomeSeatsNotAvailable_createReservationAndReserveSeats(Jdbi jdbi) {

        Reservation reservation = buildReservation();

        Set<String> seatIds = Set.of("ZE3", "ZE4");
        assertThatThrownBy(() -> theatreShowDao.createReservationAndReserveSeats(SHOW_ID, seatIds, reservation))
                .isInstanceOf(RuntimeException.class);

        verifyReservationNotCreated(jdbi, reservation);
        verifySeatsNotReserved(jdbi, SHOW_ID, seatIds, NEW_RESERVATION_ID);
    }

    private void verifyReservationCreated(Jdbi jdbi, Reservation reservation) {
        Optional<Reservation> actualReservation = jdbi.withHandle(h ->
                h.createQuery(GET_RESERVATION_BY_ID)
                        .bind("id", reservation.getId())
                        .map(new ReservationRowMapper())
                        .findOne()
        );
        assertThat(actualReservation).isNotEmpty();
    }

    private void verifyReservationNotCreated(Jdbi jdbi, Reservation reservation) {
        Optional<Reservation> actualReservation = jdbi.withHandle(h ->
                h.createQuery(GET_RESERVATION_BY_ID)
                        .bind("id", reservation.getId())
                        .map(new ReservationRowMapper())
                        .findOne()
        );
        assertThat(actualReservation).isEmpty();
    }

    private void verifySeatsNotReserved(Jdbi jdbi, String showId, Set<String> seatIds, String reservationId) {
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

    private void verifySeatsHaveBeenReserved(Jdbi jdbi, String showId, Set<String> seatIds, String reservationId) {
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

    private Reservation buildReservation() {
        return Reservation.builder()
                .id(NEW_RESERVATION_ID)
                .customerId(CUSTOMER_ID)
                .reservationTime(LocalDateTime.now())
                .totalPrice(new BigDecimal("1500"))
                .build();
    }
}
