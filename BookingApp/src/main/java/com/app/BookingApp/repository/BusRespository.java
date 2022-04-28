package com.app.BookingApp.repository;

import java.util.Optional;

import com.app.BookingApp.models.Bus;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusRespository extends CrudRepository<Bus, Long>{
    
    // public Optional<Bus> findAllUserBystartPlacAndendPlace(String startPlace, String endPlace);

    // public Optional<Bus> findBus

    
}
