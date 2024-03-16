package com.tusofia.codeinspection.controller;

import com.google.gson.Gson;
import com.sun.istack.NotNull;
import com.tusofia.codeinspection.dto.TrainDto;
import com.tusofia.codeinspection.model.Train;
import com.tusofia.codeinspection.repository.TrainRepository;
import com.tusofia.codeinspection.service.TrainService;
import org.aspectj.lang.annotation.After;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrainControllerTest {

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Gson gson;

    private MockMvc mvc;

    private static final String SOFIA = "Sofia";

    private static final String PLOVDIV = "Plovdiv";

    private static final LocalTime NINE_THIRTY_AM = LocalTime.parse("09:30:00.00");

    private static final LocalTime SIXTEEN_PM = LocalTime.parse("16:00:00.00");

    Train train;

    @BeforeEach
    void setUp() {
        train = trainRepository.save(new Train());
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test()
    void createTrain_shouldReturn_statusOk() throws Exception {
        String json = "{\n"
                + "   \"depart\":\"Sofia\",\n"
                + "   \"destination\":\"Plovdiv\",\n"
                + "    \"departTime\":\"20:43:41.453\",\n"
                + "    \"arriveTime\":\"20:43:41.453\",\n"
                + "    \"kilometers\": 250,\n"
                + "    \"totalSeats\" : 100\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.post("/train/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @After("createTrain_shouldReturn_statusOk")
    void updateTrain_shouldReturn_statusOk() throws Exception {

        String json = "{\n"
                + "   \"depart\":\"Varna\",\n"
                + "   \"destination\":\"Plovdiv\",\n"
                + "    \"departTime\":\"20:43:41.453\",\n"
                + "    \"arriveTime\":\"20:43:41.453\",\n"
                + "    \"kilometers\": 300,\n"
                + "    \"totalSeats\" : 50\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.patch("/train/update-train/" + train.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    void updateTrain_shouldReturn_statusNotFound() throws Exception {

        String json = "{\n"
                + "   \"depart\":\"Varna\",\n"
                + "   \"destination\":\"Plovdiv\",\n"
                + "    \"departTime\":\"20:43:41.453\",\n"
                + "    \"arriveTime\":\"20:43:41.453\",\n"
                + "    \"kilometers\": 300,\n"
                + "    \"totalSeats\" : 50\n"
                + "}";
        this.mvc
                .perform(MockMvcRequestBuilders.patch("/train/update-train/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @After("updateTrain_shouldReturn_statusOk")
    void deleteTrain_shouldReturn_statusNoContent() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.delete("/train/delete-train/"+train.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @After("updateTrain_shouldReturn_statusOk")
    void deleteTrain_shouldReturn_statusNotFound() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.delete("/train/delete-train/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /*@Test
    @After("login_shouldReturn_statusOk()")
    void findAllTrains_shouldReturn_statusOk() throws Exception {
        trainRepository.save(createTrainDto(SOFIA,PLOVDIV,NINE_THIRTY_AM,SIXTEEN_PM,250,100,0));
        this.mvc
                .perform(MockMvcRequestBuilders.get("/train/find-all-trains")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }*/

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
