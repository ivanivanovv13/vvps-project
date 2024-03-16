package com.tusofia.codeinspection.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class TrainDto {
    @NotNull
    private String depart;
    @NotNull
    private String destination;
    @NotNull
    private LocalTime departTime;
    @NotNull
    private LocalTime arriveTime;

    private int kilometers;
    private int totalSeats;
    private int reservedSeats;
}
