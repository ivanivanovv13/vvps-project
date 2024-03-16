package com.tusofia.codeinspection.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String depart;
    @NotNull
    private String destination;
    @NotNull
    private LocalTime departTime;
    @NotNull
    private LocalTime arriveTime;

    @NotNull
    private LocalDate date;

    private int kilometers;

    private int totalSeats;

    private int reservedSeats;

    @OneToOne(mappedBy = "train")
    private Ticket ticket;
}
