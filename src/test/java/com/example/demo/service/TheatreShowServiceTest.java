package com.example.demo.service;


import com.example.demo.dto.ReservationRequest;
import com.example.demo.dto.ReservationResponse;
import com.example.demo.model.BookingStatus;
import com.example.demo.model.Show;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.statement.SqlStatements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static com.example.demo.util.DbTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class TheatreShowServiceTest {

    private static final String SHOW_ID = "1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8";
    private static final String CUSTOMER_ID = "4b7cd538-1f31-416d-9874-6c543f183d72";

    @Autowired
    private TheatreShowService theatreShowService;

    @Autowired
    private Jdbi jdbi;

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
    }

    @Test
    void testGetShowsByName() {
        List<Show> shows = theatreShowService.findShowsByName("Wicked");
        assertThat(shows).hasSize(2);
    }

    @Test
    void givenSeatsAvailable_reserveSeats() {

        Set<String> seatIds = Set.of("ZF44", "ZF45");
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .seatIds(seatIds)
                .customerId(CUSTOMER_ID)
                .build();

        ReservationResponse reservationResponse = theatreShowService.reserveSeats(SHOW_ID, reservationRequest);
        assertThat(reservationResponse)
                .returns(CUSTOMER_ID, ReservationResponse::getCustomerId)
                .returns(seatIds, ReservationResponse::getSeatIds);

        verifyReservationCreated(jdbi, CUSTOMER_ID);
        verifySeatNotReservedWithExpectedStatus(jdbi, SHOW_ID, "ZF44", BookingStatus.RESERVED, false);
        verifySeatNotReservedWithExpectedStatus(jdbi, SHOW_ID, "ZF45", BookingStatus.RESERVED, false);

    }

    @Test
    void givenSomeSeatsNotAvailable_reserveSeats() {

        Set<String> seatIds = Set.of("ZE3", "ZE4");
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .seatIds(seatIds)
                .customerId(CUSTOMER_ID)
                .build();

        assertThatThrownBy(() -> theatreShowService.reserveSeats(SHOW_ID, reservationRequest))
                .isInstanceOf(RuntimeException.class);

        verifyReservationNotCreated(jdbi, CUSTOMER_ID);
        verifySeatNotReservedWithExpectedStatus(jdbi, SHOW_ID, "ZE3", BookingStatus.AVAILABLE, true);
        verifySeatNotReservedWithExpectedStatus(jdbi, SHOW_ID, "ZE4", BookingStatus.CLOSED, true);
    }
}
