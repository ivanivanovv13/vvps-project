package com.tusofia.codeinspection.repository;

import com.tusofia.codeinspection.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
