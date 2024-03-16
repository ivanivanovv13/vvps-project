package com.tusofia.codeinspection.service.impl;

import com.tusofia.codeinspection.enums.DiscountCardType;
import com.tusofia.codeinspection.enums.TicketType;
import com.tusofia.codeinspection.model.Ticket;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.repository.TicketRepository;
import com.tusofia.codeinspection.repository.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class TicketServiceImplTest {
    @Mock
    TrainRepository trainRepository;
    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    TicketServiceImpl underTest;

    private static final Long ID = 1L;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket();
        ticket.setId(ID);
    }

    @Test
    void availableSeats_shouldReturn_correctNumberOfSeats() {
        Train train = createTrain(100, 10);
        int result = underTest.availableSeats(train);

        assertEquals(90, result);
    }

    @Test
    void createTicket_shouldInvoke_repositorySave_once() {
        underTest.createTicket(ticket);

        Mockito.verify(ticketRepository, Mockito.times(1)).save(ticket);
    }

    @Test
    void deleteTicket_shouldInvoke_repositoryDeleteById_once() {
        underTest.deleteTicket(ID);

        Mockito.verify(ticketRepository, Mockito.times(1)).deleteById(ID);
    }

    @Test
    void calculatePriceOfRoute_shouldThrow_runtimeException() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> underTest.calculatePriceOfRoute(0, TicketType.ONE_WAY));

        assertEquals("distanceInKilometers is invalid", ex.getMessage());
    }

    @Test
    void calculatePriceOfRoute_shouldReturn_correctPrice_withOneWayTicket_under100KM() {

        double price = underTest.calculatePriceOfRoute(90, TicketType.ONE_WAY);

        assertEquals(90 * 0.15, price);
    }

    @Test
    void calculatePriceOfRoute_shouldReturn_correctPrice_withOneWayTicket_over100KM() {

        double price = underTest.calculatePriceOfRoute(150, TicketType.ONE_WAY);

        assertEquals(150 * 0.15*0.9, price);
    }

    @Test
    void calculatePriceOfRoute_shouldReturn_correctPrice_withTwiWayTicket_over100KM() {

        double price = underTest.calculatePriceOfRoute(150, TicketType.TWO_WAY);

        assertEquals(150 * 0.25*0.9, price);
    }
    @Test
    void calculatePriceOfTicket_shouldReturn_correctPrice_withoutDiscountCard_withoutTimeDiscount_forOnePerson(){
        double price =underTest.calculatePriceOfTicket(100, LocalTime.parse("09:25:00.00"), DiscountCardType.NONE,false,1);

        assertEquals(100,price);
    }

    @Test
    void calculatePriceOfTicket_shouldReturn_correctPrice_withoutDiscountCard_withTimeDiscount_forOnePerson(){
        double price =underTest.calculatePriceOfTicket(100, LocalTime.parse("09:45:00.00"), DiscountCardType.NONE,false,1);

        assertEquals(95,price);
    }

    @Test
    void calculatePriceOfTicket_shouldReturn_correctPrice_withoutDiscountCard_withOutTimeDiscount_forFivePerson(){
        double price = underTest.calculatePriceOfTicket(100, LocalTime.parse("09:25:00.00"), DiscountCardType.NONE,false,5);

        assertEquals(100*5,price);
    }

    @Test
    void calculatePriceOfTicket_shouldReturn_correctPrice_withDiscountCardFamily_withOutTimeDiscount_forThreePerson_andChildUnderSixteen(){
        double price = underTest.calculatePriceOfTicket(100, LocalTime.parse("09:25:00.00"), DiscountCardType.FAMILY,true,3);

        assertEquals(100*3*0.5,price);
    }

    @Test
    void calculatePriceOfTicket_shouldReturn_correctPrice_withDiscountCardFamily_withOutTimeDiscount_forThreePerson_andNoChildUnderSixteen(){
        double price = underTest.calculatePriceOfTicket(100, LocalTime.parse("09:25:00.00"), DiscountCardType.FAMILY,false,3);

        assertEquals(100*3*0.9,price);
    }

    @Test
    void calculatePriceOfTicket_shouldReturn_correctPrice_withDiscountCardElderly_withOutTimeDiscount_forOnePerson(){
        double price = underTest.calculatePriceOfTicket(100, LocalTime.parse("09:25:00.00"), DiscountCardType.ELDERLY,false,1);

        assertEquals(100*0.66,price);
    }

    @Test
    void calculatePriceOfTicket_shouldReturn_correctPrice_withDiscountCardElderly_withOutTimeDiscount_forFivePerson(){
        double price = underTest.calculatePriceOfTicket(100, LocalTime.parse("09:25:00.00"), DiscountCardType.ELDERLY,false,5);

        assertEquals(100*0.66+4*100,price);
    }

    private Train createTrain(int totalSeats, int reservedSeats) {
        Train train = new Train();
        train.setReservedSeats(reservedSeats);
        train.setTotalSeats(totalSeats);

        return train;
    }
}
