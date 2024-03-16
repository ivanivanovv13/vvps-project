package com.tusofia.codeinspection.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReservationResponse {

    private UserDto user;

    private TicketResponse ticket;
}
