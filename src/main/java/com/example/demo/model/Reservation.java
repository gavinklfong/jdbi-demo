package com.example.demo.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static java.util.Optional.ofNullable;

@EqualsAndHashCode
@Builder
@Value
public class Reservation {
    String id;
    String customerId;
    @EqualsAndHashCode.Exclude
    BigDecimal totalPrice;
    LocalDateTime reservationTime;

    @EqualsAndHashCode.Include
    private BigDecimal getTotalPriceForEquals() {
        return ofNullable(totalPrice).map(BigDecimal::stripTrailingZeros).orElse(null);
    }
}
