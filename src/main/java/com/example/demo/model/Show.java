package com.example.demo.model;

import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public class Show {
    String id;
    String location;
    LocalDateTime startTime;
    Set<Seat> seats;
    Duration duration;
}
