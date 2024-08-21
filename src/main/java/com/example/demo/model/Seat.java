package com.example.demo.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public class Seat {
    String showId;
    String seatId;
    String region;
    BookingStatus status;
    String reservationRef;
    BigDecimal price;
}
