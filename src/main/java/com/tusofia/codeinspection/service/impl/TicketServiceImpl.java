package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.enums.DiscountCardType;
import com.tusofia.codeinspection.enums.TicketType;
import com.tusofia.codeinspection.model.Ticket;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.repository.TicketRepository;
import com.tusofia.codeinspection.repository.TrainRepository;
import com.tusofia.codeinspection.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@AllArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private static final LocalTime NINE_THIRTY_AM = LocalTime.parse("09:30:00.00");
    private static final LocalTime SIXTEEN_PM = LocalTime.parse("16:00:00.00");
    private static final LocalTime SEVEN_THIRTY_PM = LocalTime.parse("19:30:00.00");
    private static final double ONE_WAY_PRICE_PER_KILOMETER = 0.15;
    private static final double TWO_WAY_PRICE_PER_KILOMETER = 0.25;

    TrainRepository trainRepository;

    TicketRepository ticketRepository;

    @Override
    public int availableSeats(Train train) {
        return train.getTotalSeats() - train.getReservedSeats();
    }

    @Override
    public double calculatePriceOfRoute(int distanceInKilometers, TicketType type) {
        if (distanceInKilometers <= 0)
            throw new RuntimeException("distanceInKilometers is invalid");

        double pricePerKilometer = (type == TicketType.ONE_WAY) ? ONE_WAY_PRICE_PER_KILOMETER : TWO_WAY_PRICE_PER_KILOMETER;
        return distanceInKilometers >= 100 ? pricePerKilometer * distanceInKilometers * 0.9 : pricePerKilometer * distanceInKilometers;
    }

    @Override
    public double calculatePriceOfTicket(double priceOfRoute, LocalTime departTime, DiscountCardType cardType, boolean childUnderSixteen, int numberOfSeats) {

        if (departTime.isAfter(NINE_THIRTY_AM) && departTime.isBefore(SIXTEEN_PM) || departTime.isAfter(SEVEN_THIRTY_PM))
            priceOfRoute -= priceOfRoute * 0.05;

        switch (cardType) {
            case FAMILY:
                return childUnderSixteen
                        ? (priceOfRoute - priceOfRoute * 0.5) * numberOfSeats
                        : (priceOfRoute - priceOfRoute * 0.1) * numberOfSeats;
            case ELDERLY:
                return numberOfSeats == 1
                        ? priceOfRoute - priceOfRoute * 0.34
                        : (priceOfRoute - priceOfRoute * 0.34) + (priceOfRoute * --numberOfSeats);
            default:
                return priceOfRoute * numberOfSeats;
        }
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
