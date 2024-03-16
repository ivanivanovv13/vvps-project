package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.model.Reservation;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.repository.TrainRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TrainServiceImplTest {
    @Mock
    TrainRepository trainRepository;

    @InjectMocks
    TrainServiceImpl underTest;

    private Train train;

    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        train = new Train();
        train.setId(ID);
        train.setReservedSeats(0);
    }

    @Test
    void createTrain_shouldInvoke_repositorySave_once() {
        underTest.createTrain(train);

        Mockito.verify(trainRepository, Mockito.times(1)).save(train);
    }

    @Test
    void updateTrain_shouldInvoke_repositorySave_once() {

        underTest.updateTrain(train);

        Mockito.verify(trainRepository, Mockito.times(1)).save(train);
    }

    @Test
    void deleteTrain_shouldInvoke_repositoryDeleteById_once() {
        underTest.deleteTrain(ID);

        Mockito.verify(trainRepository, Mockito.times(1)).deleteById(ID);
    }


    @Test
    void findTrain_shouldReturn_correctTrain() {
        Mockito.when(trainRepository.findById(ID)).thenReturn(Optional.of(train));

        Optional<Train> foundTrain = underTest.findTrain(ID);
        assertTrue(foundTrain.isPresent());
        assertEquals(train, foundTrain.get());
    }

    @Test
    void findTrain_shouldReturn_emptyOptional() {
        Mockito.when(trainRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<Train> foundTrain = underTest.findTrain(ID);
        assertTrue(foundTrain.isEmpty());
    }

    @Test
    void reserveSeats_shouldInvoke_repositorySave_once() {
        underTest.reserveSeats(train, 5);

        Mockito.verify(trainRepository, Mockito.times(1)).save(train);
    }

    @Test
    void reserveSeats_shouldInvoke_correctTrain() {
        Mockito.when(trainRepository.save(train)).thenReturn(train);
        Train result = underTest.reserveSeats(train, 5);

        assertEquals(5,result.getReservedSeats());
    }

    @Test
    void findAll_shouldReturn_emptyPage() {
        Pageable pageable = PageRequest.of(0, 8);
        Mockito.when(trainRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<Train> result = underTest.findAll(pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllReservations_shouldReturn_pageWithOneReservation() {
        Pageable pageable = PageRequest.of(0, 1);
        Mockito.when(trainRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(train), pageable, 1));

        Page<Train> result = underTest.findAll(pageable);

        assertEquals(1,result.getSize());
        assertEquals(1,result.getTotalElements());
        assertEquals(1,result.getTotalPages());
    }
}
