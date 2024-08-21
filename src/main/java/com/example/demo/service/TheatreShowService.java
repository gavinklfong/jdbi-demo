package com.example.demo.service;

import com.example.demo.dao.TheatreShowDao;
import com.example.demo.dto.ReservationRequest;
import com.example.demo.dto.ReservationResponse;
import com.example.demo.model.Reservation;
import com.example.demo.model.Seat;
import com.example.demo.model.Show;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TheatreShowService {

    private final TheatreShowDao theatreShowDao;

    public Set<Show> findShowsByName(String showName) {
        return null;
    }

    public Show findShowById(String showId) {
        return null;
    }

    @Transactional
    public ReservationResponse reserveSeats(String showId, ReservationRequest reservationRequest) {
        List<Seat> seats = theatreShowDao.getSeatsByShowIdAndSeatId(showId, reservationRequest.getSeatIds());
        BigDecimal totalPrice = seats.stream()
                .map(Seat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .customerId(reservationRequest.getCustomerId())
                .totalPrice(totalPrice)
                .reservationTime(LocalDateTime.now())
                .build();

        theatreShowDao.insertReservation(reservation);

        theatreShowDao.reserveSeats(showId, reservationRequest.getSeatIds(), reservation.getId());

        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .seatIds(reservationRequest.getSeatIds())
                .customerId(reservation.getCustomerId())
                .build();
    }


}
