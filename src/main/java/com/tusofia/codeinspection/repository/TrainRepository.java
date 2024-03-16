package com.tusofia.codeinspection.repository;

import com.tusofia.codeinspection.model.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {

    Page<Train> findAll(Pageable pageable);

    Optional<Train> findById(Long id);
}
