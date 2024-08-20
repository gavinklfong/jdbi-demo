package com.example.demo.dto;

import lombok.Value;

import java.util.Set;

@Value
public class ReservationRequest {
    String customerId;
    Set<String> seatIds;
}
