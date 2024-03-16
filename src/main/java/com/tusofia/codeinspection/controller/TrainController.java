package com.tusofia.codeinspection.controller;

import com.tusofia.codeinspection.dto.TrainDto;
import com.tusofia.codeinspection.exception.TrainNotFoundException;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.service.TrainService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/train")
@AllArgsConstructor
public class TrainController {

    ModelMapper modelMapper;

    TrainService trainService;

    @PostMapping("/create")
    public ResponseEntity<TrainDto> createTrain(@RequestBody TrainDto trainRequest) {
        Train train = modelMapper.map(trainRequest, Train.class);
        train = trainService.createTrain(train);
        TrainDto result = modelMapper.map(train, TrainDto.class);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/update-train/{trainId}")
    public ResponseEntity<TrainDto> updateTrain(@PathVariable Long trainId, @RequestBody TrainDto trainRequest) {
        Train train = trainService.findTrain(trainId)
                .orElseThrow(() -> new TrainNotFoundException("Train not found"));
        modelMapper.map(trainRequest, train);
        train = trainService.updateTrain(train);
        TrainDto result = modelMapper.map(train, TrainDto.class);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete-train/{trainId}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long trainId) {
        if (trainService.findTrain(trainId).isEmpty()) {
            throw new TrainNotFoundException("Train not found");
        }
        trainService.deleteTrain(trainId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/find-all-trains")
    public ResponseEntity<Page<TrainDto>> findAllTrains(Pageable pageable) {
        Page<Train> trains = trainService.findAll(pageable);
        List<TrainDto> trainDtos = trains.stream().map(train -> modelMapper.map(train, TrainDto.class)).collect(Collectors.toList());
        Page<TrainDto> result = new PageImpl<>(trainDtos, pageable, trainDtos.size());

        return ResponseEntity.ok(result);
    }

}
