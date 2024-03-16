package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.model.Reservation;
import com.tusofia.codeinspection.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class ReservationServiceImplTest {
    @Mock
    private ReservationRepository reservationRepository;
    @InjectMocks
    private ReservationServiceImpl underTest;

    private Reservation reservation;

    private static final Long ID = 1L;

    private static final String userEmail = "test@email.com";

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
    }

    @Test
    void createReservation_shouldInvoke_repositorySave_once() {
        underTest.createReservation(reservation);

        Mockito.verify(reservationRepository, Mockito.times(1)).save(reservation);
    }

    @Test
    void updateReservation_shouldInvoke_repositorySave_once() {
        underTest.updateReservation(reservation);

        Mockito.verify(reservationRepository, Mockito.times(1)).save(reservation);
    }

    @Test
    void deleteAuthority_shouldInvoke_repositoryDelete_once() {
        underTest.deleteReservation(reservation);

        Mockito.verify(reservationRepository, Mockito.times(1)).delete(reservation);
    }

    @Test
    void findReservation_shouldReturn_correctReservation() {
        reservation.setId(ID);
        Mockito.when(reservationRepository.findById(ID)).thenReturn(Optional.of(reservation));
        Optional<Reservation> foundReservation = underTest.findReservation(ID);

        assertTrue(foundReservation.isPresent());
        assertEquals(ID, reservation.getId());
    }

    @Test
    void findReservation_shouldReturn_emptyOptional() {
        Mockito.when(reservationRepository.findById(ID)).thenReturn(Optional.empty());
        Optional<Reservation> foundReservation = underTest.findReservation(ID);

        assertTrue(foundReservation.isEmpty());
    }

    @Test
    void findAllReservations_shouldReturn_emptyPage() {
        Pageable pageable = PageRequest.of(0, 8);
        Mockito.when(reservationRepository.findByUserEmail(userEmail, pageable)).thenReturn(Page.empty());

        Page<Reservation> result = underTest.findAllReservations(userEmail, pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllReservations_shouldReturn_pageWithOneReservation() {
        Pageable pageable = PageRequest.of(0, 1);
        Mockito.when(reservationRepository.findByUserEmail(userEmail, pageable))
                .thenReturn(new PageImpl<>(List.of(reservation), pageable, 1));

        Page<Reservation> result = underTest.findAllReservations(userEmail, pageable);

        assertEquals(1,result.getSize());
        assertEquals(1,result.getTotalElements());
        assertEquals(1,result.getTotalPages());
    }
}
