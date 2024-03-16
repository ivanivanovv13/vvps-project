package com.tusofia.codeinspection.dto;

import com.sun.istack.NotNull;
import com.tusofia.codeinspection.enums.DiscountCardType;
import com.tusofia.codeinspection.enums.TicketType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public class TicketDto {

    @NotNull
    private TicketType ticketType;

    @NotNull
    private DiscountCardType discountCardType;

    @NotNull
    private boolean childIsUnderSixteen;

    private int numberOfSeats;
}
