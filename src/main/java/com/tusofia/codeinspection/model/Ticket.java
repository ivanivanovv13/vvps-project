package com.tusofia.codeinspection.model;

import com.sun.istack.NotNull;
import com.tusofia.codeinspection.enums.DiscountCardType;
import com.tusofia.codeinspection.enums.TicketType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double price;

    @NotNull
    private TicketType ticketType;

    @NotNull
    private DiscountCardType discountCardType;

    private boolean childIsUnderSixteen;

    private int numberOfSeats;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id",referencedColumnName = "id")
    private Train train;

    @OneToOne(mappedBy = "ticket")
    private Reservation reservation;
}
