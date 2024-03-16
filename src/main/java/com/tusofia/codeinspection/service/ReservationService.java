package com.tusofia.codeinspection.service;

import com.tusofia.codeinspection.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    Reservation updateReservation(Reservation reservation);
    void deleteReservation(Reservation reservation);

    Page<Reservation> findAllReservations(String userEmail, Pageable pageable);

    Optional<Reservation> findReservation(Long id);
}
