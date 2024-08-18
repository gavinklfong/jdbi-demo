package com.example.demo.model;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Seat {
    String id;
    String region;
    BookingStatus status;
    String reservationRef;
    BigDecimal price;
}
