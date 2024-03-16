package com.tusofia.codeinspection.service;

import com.tusofia.codeinspection.enums.DiscountCardType;
import com.tusofia.codeinspection.enums.TicketType;
import com.tusofia.codeinspection.model.Ticket;
import com.tusofia.codeinspection.model.Train;

import java.time.LocalTime;

public interface TicketService {

    int availableSeats(Train train);

    double calculatePriceOfRoute(int kilometers, TicketType type);

    double calculatePriceOfTicket(double priceOfRoute, LocalTime departTime, DiscountCardType cardType, boolean childUnderSixteen, int numberOfSeats);

    Ticket createTicket(Ticket ticket);

    void deleteTicket(Long id);
}
