package com.tusofia.codeinspection.controller;


import com.tusofia.codeinspection.dto.ReservationResponse;
import com.tusofia.codeinspection.dto.TicketDto;
import com.tusofia.codeinspection.exception.TrainNotFoundException;
import com.tusofia.codeinspection.exception.UserNotFoundException;
import com.tusofia.codeinspection.model.Reservation;
import com.tusofia.codeinspection.model.Ticket;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.model.User;
import com.tusofia.codeinspection.service.ReservationService;
import com.tusofia.codeinspection.service.TicketService;
import com.tusofia.codeinspection.service.TrainService;
import com.tusofia.codeinspection.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {

    TrainService trainService;
    UserService userService;
    TicketService ticketService;

    ReservationService reservationService;

    ModelMapper modelMapper;

    @PostMapping(path = "/train/{trainId}/create-reservation/{userEmail}")
    public ResponseEntity<ReservationResponse> createReservation(@PathVariable("trainId") Long trainId,
                                                                 @PathVariable("userEmail") String userEmail,
                                                                 @RequestBody TicketDto ticketRequest) {
        User user = userService.findUser(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found!"));
        Train train = trainService.findTrain(trainId)
                .orElseThrow(() -> new TrainNotFoundException("Train not found"));

        if (ticketService.availableSeats(train) < ticketRequest.getNumberOfSeats()) {
            throw new RuntimeException("There aren't available seats!");
        }

        Ticket ticket = createTicket(ticketRequest, trainService.reserveSeats(train, ticketRequest.getNumberOfSeats()));
        Reservation reservation = new Reservation(null, ticket, user);

        return new ResponseEntity<>(modelMapper.map(reservationService.createReservation(reservation), ReservationResponse.class), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/train/{trainId}/update-reservation/{reservationId}")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable("trainId") Long trainId,
                                                                 @PathVariable("reservationId") Long reservationId,
                                                                 @RequestBody TicketDto ticketRequest) {
        Train train = trainService.findTrain(trainId)
                .orElseThrow(() -> new TrainNotFoundException("Train not found"));
        Reservation reservation = reservationService.findReservation(reservationId)
                .orElseThrow(() -> new RuntimeException("No reservation with given id!"));

        if (ChronoUnit.MINUTES.between(LocalTime.now(), train.getDepartTime()) < 30) {
            throw new RuntimeException("Can't reserve train with given id!");
        }

        if (ticketService.availableSeats(train) < ticketRequest.getNumberOfSeats()) {
            throw new RuntimeException("There aren't available seats!");
        }

        removeReservedTrainSeats(reservation.getTicket().getTrain(), reservation.getTicket().getNumberOfSeats());
        Ticket oldTicket = reservation.getTicket();

        Ticket ticket = createTicket(ticketRequest, trainService.reserveSeats(train, ticketRequest.getNumberOfSeats()));
        reservation.setTicket(ticket);
        ReservationResponse result = modelMapper.map(reservationService.updateReservation(reservation), ReservationResponse.class);
        ticketService.deleteTicket(oldTicket.getId());

        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "{userEmail}/find-all-reservations")
    public ResponseEntity<Page<ReservationResponse>> findAllReservations(@PathVariable("userEmail") String userEmail, Pageable pageable) {
        if (!userService.findUser(userEmail).isPresent()) {
            throw new UserNotFoundException("User with email " + userEmail + " not found!");
        }
        Page<Reservation> reservations = reservationService.findAllReservations(userEmail, pageable);

        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponse.class))
                .collect(Collectors.toList());
        Page<ReservationResponse> result = new PageImpl<>(reservationResponses, pageable, reservationResponses.size());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/{reservationId}/delete-reservation")
    public ResponseEntity<Void> deleteReservation(@PathVariable("reservationId") Long reservationId) {
        Reservation reservation = reservationService.findReservation(reservationId)
                .orElseThrow(() -> new RuntimeException("No reservation with given id!"));
        reservationService.deleteReservation(reservation);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Ticket createTicket(TicketDto ticketRequest, Train train) {
        Ticket ticket = modelMapper.map(ticketRequest, Ticket.class);
        ticket.setTrain(train);
        ticket.setPrice(ticketService.calculatePriceOfTicket(
                ticketService.calculatePriceOfRoute(train.getKilometers(), ticketRequest.getTicketType()),
                train.getDepartTime(),
                ticket.getDiscountCardType(),
                ticketRequest.isChildIsUnderSixteen(),
                ticketRequest.getNumberOfSeats()
        ));
        return ticketService.createTicket(ticket);
    }

    private void removeReservedTrainSeats(Train train, int numberOfSeats) {
        train.setReservedSeats(train.getReservedSeats() - numberOfSeats);
        trainService.updateTrain(train);
    }
}
