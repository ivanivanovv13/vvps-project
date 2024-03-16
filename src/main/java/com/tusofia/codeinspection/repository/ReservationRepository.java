package com.tusofia.codeinspection.repository;

import com.tusofia.codeinspection.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByUserEmail(String email,Pageable pageable);
}
