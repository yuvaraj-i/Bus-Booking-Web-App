package com.app.BookingApp.repository;

import java.util.Set;

import com.app.BookingApp.models.Bus;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusRespository extends CrudRepository<Bus, Long>{

    public Set<Bus> findAllByStartLocation(String boardingLocation);
    public Set<Bus> findAllByEndLocation(String destinationLocation);
    
}
