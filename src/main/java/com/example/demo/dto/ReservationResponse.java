package com.example.demo.dto;

import lombok.Value;

import java.util.Set;

@Value
public class ReservationResponse {
    String customerId;
    Set<String> seatIds;
    String reservationId;
}
