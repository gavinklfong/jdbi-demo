package com.example.demo.controller;

import com.example.demo.dto.ReservationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import org.jdbi.v3.core.statement.SqlStatements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TheatreTheatreShowControllerTest {

    private static final String SHOW_ID = "1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8";
    private static final String CUSTOMER_ID = "5e9d267a-84cb-4317-bf75-1ce69d425455";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Jdbi jdbi;

    private ObjectMapper objectMapper = new ObjectMapper();

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
    void givenSeatsAvailable_reserveSeats() throws Exception {

        Set<String> seatIds = Set.of("ZF44", "ZF45");
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .seatIds(seatIds)
                .customerId(CUSTOMER_ID)
                .build();

        mockMvc.perform(post(String.format("/shows/%s/reservation", SHOW_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationRequest)))
                .andExpect(status().isOk());

    }
}
