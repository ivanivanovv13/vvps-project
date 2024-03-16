package com.tusofia.codeinspection.controller;

import com.google.gson.Gson;
import com.tusofia.codeinspection.model.Authority;
import com.tusofia.codeinspection.model.Reservation;
import com.tusofia.codeinspection.model.Ticket;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.model.User;
import com.tusofia.codeinspection.repository.ReservationRepository;
import com.tusofia.codeinspection.repository.TicketRepository;
import com.tusofia.codeinspection.repository.TrainRepository;
import com.tusofia.codeinspection.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {
    @Autowired
    TrainRepository trainRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private static final String SOFIA = "Sofia";

    private static final String PLOVDIV = "Plovdiv";

    private static final LocalTime NINE_THIRTY_AM = LocalTime.parse("09:30:00.00");

    private static final LocalTime SIXTEEN_PM = LocalTime.parse("16:00:00.00");
    private Train train;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        userRepository.save(createUser("test@mail.bg", "1234", null));
        train = trainRepository.save(createTrainDto(SOFIA, PLOVDIV, SIXTEEN_PM, NINE_THIRTY_AM, 250, 100, 0));
        Ticket ticket = new Ticket();
        reservation = new Reservation();
        ticket.setTrain(train);
        ticketRepository.save(ticket);
        reservation.setTicket(ticket);
        reservation = reservationRepository.save(reservation);

        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test()
    void createReservation_shouldReturn_statusOk() throws Exception {

        String json = "{\n"
                + "\"ticketType\":\"TWO_WAY\",\n"
                + "\"discountCardType\":\"ELDERLY\",\n"
                + "\"childIsUnderSixteen\": true,\n"
                + "\"numberOfSeats\": 2\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.post("/reservation/train/" + train.getId() + "/create-reservation/test@mail.bg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test()
    void createReservation_shouldReturn_statusNotFound() throws Exception {
        String json = "{\n"
                + "\"ticketType\":\"TWO_WAY\",\n"
                + "\"discountCardType\":\"ELDERLY\",\n"
                + "\"childIsUnderSixteen\": true,\n"
                + "\"numberOfSeats\": 2\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.post("/reservation/train/" + train.getId() + "/create-reservation/wrongemail@abv.bg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void updateReservation_shouldReturn_statusOk() throws Exception {
        String json = "{\n"
                + "\"ticketType\":\"TWO_WAY\",\n"
                + "\"discountCardType\":\"ELDERLY\",\n"
                + "\"childIsUnderSixteen\": true,\n"
                + "\"numberOfSeats\": 2\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.patch("/reservation/train/" + train.getId() + "/update-reservation/" + reservation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void updateReservation_shouldReturn_statusNotFound() throws Exception {
        String json = "{\n"
                + "\"ticketType\":\"TWO_WAY\",\n"
                + "\"discountCardType\":\"ELDERLY\",\n"
                + "\"childIsUnderSixteen\": true,\n"
                + "\"numberOfSeats\": 2\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.patch("/reservation/train/999/update-reservation/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findAllReservations_shouldReturn_statusNotFound() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.get("/reservation/test@yahoo.com/find-all-reservations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findAllReservations_shouldReturn_statusOk() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.get("/reservation/test@mail.bg/find-all-reservations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteReservation_shouldReturn_statusNoContent() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.delete("/reservation/" + reservation.getId() + "/delete-reservation")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void deleteReservation_shouldReturn_statusNotFound() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.delete("/reservation/" + reservation.getId() + "/delete-reservation")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private User createUser(String email, String password, Set<Authority> authorities) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setAuthority(authorities);
        return user;
    }

    private Train createTrainDto(String depart, String destination, LocalTime departTime, LocalTime arriveTime, int kilometers, int totalSeats, int reservedSeats) {
        Train train = new Train();
        train.setDepart(depart);
        train.setDestination(destination);
        train.setArriveTime(arriveTime);
        train.setDepartTime(departTime);
        train.setKilometers(kilometers);
        train.setTotalSeats(totalSeats);
        train.setReservedSeats(reservedSeats);

        return train;
    }
}
