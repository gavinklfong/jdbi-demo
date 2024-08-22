package com.example.demo.service;

import com.example.demo.dao.TheatreShowDao;
import com.example.demo.dto.ReservationRequest;
import com.example.demo.dto.ReservationResponse;
import com.example.demo.model.Reservation;
import com.example.demo.model.Seat;
import com.example.demo.model.TheatreShow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TheatreShowService {

    private final TheatreShowDao theatreShowDao;

    public List<TheatreShow> findShowsByName(String showName) {
        return theatreShowDao.searchShowByName(showName);
    }

    public Optional<TheatreShow> findShowById(String showId) {
        return theatreShowDao.getShowById(showId);
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

        int reservationCount = theatreShowDao.insertReservation(reservation);
        if (reservationCount != 1) throw new RuntimeException("fail to create reservation");

        int reservedSeatCount = theatreShowDao.reserveSeats(showId, reservationRequest.getSeatIds(), reservation.getId());
        if (reservedSeatCount < reservationRequest.getSeatIds().size()) throw new RuntimeException("fail to reserve seats");

        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .seatIds(reservationRequest.getSeatIds())
                .customerId(reservation.getCustomerId())
                .build();
    }


}
