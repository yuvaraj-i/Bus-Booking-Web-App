package com.app.BookingApp.repository;

import java.util.List;
import java.util.Optional;

import com.app.BookingApp.models.Bus;
import com.app.BookingApp.models.Seat;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends CrudRepository<Seat,Long>{

    public Iterable<Seat> findByBusId(long busId);
    
}
