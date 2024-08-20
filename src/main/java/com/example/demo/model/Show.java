package com.example.demo.model;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Value
public class Show {
    String id;
    String location;
    LocalDateTime startTime;
    Duration duration;
}
