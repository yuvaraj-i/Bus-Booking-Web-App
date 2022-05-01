package com.app.BookingApp.repository;

import com.app.BookingApp.models.Ticket;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketReposistory extends CrudRepository<Ticket, Long>{
    
}
