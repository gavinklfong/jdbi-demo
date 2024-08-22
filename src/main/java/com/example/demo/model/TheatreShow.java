package com.example.demo.model;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@Value
public class TheatreShow {
    String id;
    String name;
    String location;
    LocalDateTime startTime;
    Duration duration;
}
