package com.example.demo.model;

import lombok.Value;

import java.util.Set;

@Value
public class ReservationOrder {
    String id;
    String customerId;
    String showId;
    Set<Seat> seats;
}
