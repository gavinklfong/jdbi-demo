package com.example.demo.controller;

import com.example.demo.dto.ReservationRequest;
import com.example.demo.dto.ReservationResponse;
import com.example.demo.model.Reservation;
import com.example.demo.model.Show;
import com.example.demo.service.TheatreShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
public class TheatreShowController {

    private final TheatreShowService theatreShowService;

    @GetMapping("/shows")
    public Set<Show> findShows(@RequestParam String name) {
        return theatreShowService.findShowsByName(name);
    }

    @GetMapping("/shows/{id}")
    public Show findShowById(@PathVariable String id) {
        return theatreShowService.findShowById(id);
    }

    @PostMapping("/shows/{id}/reservation")
    public ReservationResponse reserveSeats(@PathVariable String id, @RequestBody ReservationRequest reservationRequest) {
        return theatreShowService.reserveSeats(id, reservationRequest);
    }

}
