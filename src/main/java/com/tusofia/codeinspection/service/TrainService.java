package com.tusofia.codeinspection.service;

import com.tusofia.codeinspection.model.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.Optional;

public interface TrainService {

    Train createTrain(Train train);

    Train updateTrain(Train train);

    void deleteTrain(Long trainId);

    Optional<Train> findTrain(Long trainId);

    Page<Train> findAll(Pageable pageable);

    Train reserveSeats(Train train, int numberOfSets);
}
