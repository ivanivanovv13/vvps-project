package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.model.Reservation;
import com.tusofia.codeinspection.repository.ReservationRepository;
import com.tusofia.codeinspection.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    @Override
    public Page<Reservation> findAllReservations(String userEmail, Pageable pageable) {
        return reservationRepository.findByUserEmail(userEmail, pageable);
    }

    @Override
    public Optional<Reservation> findReservation(Long id) {
        return reservationRepository.findById(id);
    }

}
