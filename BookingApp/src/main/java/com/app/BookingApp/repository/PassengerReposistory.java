package com.app.BookingApp.repository;

import com.app.BookingApp.models.Passenger;

import org.springframework.data.repository.CrudRepository;

public interface PassengerReposistory extends CrudRepository<Passenger, Long>{

    public Iterable<Passenger> findByTicketId(Long id);
    
}
