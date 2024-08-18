package com.example.demo.controller;

import com.example.demo.model.Reservation;
import com.example.demo.model.Seat;
import com.example.demo.model.Show;
import com.example.demo.service.TheatreShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
public class TheatreShowController {

    private final TheatreShowService theatreShowService;

    @GetMapping("/theatres/{}/shows")
    public Set<Show> findShowByTheatre(String theatre) {
        return null;
    }

    @GetMapping("/theatres/{}/shows/{}")
    public Show findShowByTheatreAndShow(String theatre, String show) {
        return null;
    }

    @GetMapping("/theatres/{}/shows/{}/seats")
    public Set<Seat> findSeats(String theatre, String show) {
        return null;
    }

    @PostMapping("/theatres/{}/shows/{}/reservation")
    public Reservation reserveSeats(String theatre, String show) {
        return null;
    }

}
