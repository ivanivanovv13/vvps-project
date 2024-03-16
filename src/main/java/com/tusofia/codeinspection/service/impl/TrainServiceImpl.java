package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.repository.TrainRepository;
import com.tusofia.codeinspection.service.TrainService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TrainServiceImpl implements TrainService {

    TrainRepository trainRepository;

    @Override
    public Train createTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public Train updateTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public void deleteTrain(Long trainId) {
        trainRepository.deleteById(trainId);
    }

    @Override
    public Page<Train> findAll(Pageable pageable) {
        return trainRepository.findAll(pageable);
    }

    @Override
    public Optional<Train> findTrain(Long trainId) {
        return trainRepository.findById(trainId);
    }

    @Override
    public Train reserveSeats(Train train, int numberOfSets) {
        train.setReservedSeats(train.getReservedSeats() + numberOfSets);
        return trainRepository.save(train);
    }
}
