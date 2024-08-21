package com.example.demo.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class ReservationResponse {
    String customerId;
    Set<String> seatIds;
    String reservationId;
}
