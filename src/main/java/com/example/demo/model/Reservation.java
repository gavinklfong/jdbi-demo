package com.example.demo.model;

import lombok.Value;

import java.util.Set;

@Value
public class Reservation {
    String id;
    String customerId;
    String showId;
    Set<Seat> seats;
}
