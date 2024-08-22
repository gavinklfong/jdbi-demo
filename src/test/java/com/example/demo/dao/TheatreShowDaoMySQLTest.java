package com.example.demo.dao;


import com.example.demo.model.Reservation;
import com.example.demo.model.Seat;
import com.example.demo.model.Show;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.statement.SqlStatements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.demo.util.DbTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class TheatreShowDaoMySQLTest {

    private static final String SHOW_ID = "1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8";
    private static final String NEW_RESERVATION_ID = "6e1becbd-6acf-453f-a37e-1d2b5a17800b";
    private static final String CUSTOMER_ID = "5e9d267a-84cb-4317-bf75-1ce69d425455";

    @Container
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(
            DockerImageName.parse("mysql").withTag("latest"));

    private final Jdbi jdbi = Jdbi.create(MYSQL_CONTAINER.getJdbcUrl(), MYSQL_CONTAINER.getUsername(), MYSQL_CONTAINER.getPassword());

    private TheatreShowDao theatreShowDao;

    @BeforeEach
    void setup() {
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
    void testInsertReservation() {
        Reservation reservation = buildReservation();
        int count = theatreShowDao.insertReservation(reservation);
        assertThat(count).isEqualTo(1);

        verifyReservationCreated(jdbi, reservation);
    }

    @Test
    void givenAvailableSeats_testReserveSeats() {
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
    void givenSeatAvailable_createReservationAndReserveSeats() {

        Reservation reservation = buildReservation();

        Set<String> seatIds = Set.of("ZF44", "ZF45");
        theatreShowDao.createReservationAndReserveSeats(SHOW_ID, seatIds, reservation);

        verifyReservationCreated(jdbi, reservation);
        verifySeatsHaveBeenReserved(jdbi, SHOW_ID, seatIds, NEW_RESERVATION_ID);
    }

    @Test
    void givenSomeSeatsNotAvailable_createReservationAndReserveSeats() {

        Reservation reservation = buildReservation();

        Set<String> seatIds = Set.of("ZE3", "ZE4");
        assertThatThrownBy(() -> theatreShowDao.createReservationAndReserveSeats(SHOW_ID, seatIds, reservation))
                .isInstanceOf(RuntimeException.class);

        verifyReservationNotCreated(jdbi, reservation);
        verifySeatsNotReserved(jdbi, SHOW_ID, seatIds, NEW_RESERVATION_ID);
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
