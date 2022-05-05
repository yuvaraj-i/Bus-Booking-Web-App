package com.app.BookingApp.repository;

import java.util.ArrayList;
import java.util.Set;

import com.app.BookingApp.models.Bus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRespository extends CrudRepository<Bus, Long> {

    public Set<Bus> findAllByStartLocation(String boardingLocation);

    public Set<Bus> findAllByEndLocation(String destinationLocation);

    @Query("FROM Bus WHERE startLocation = :sLocation and endLocation = :eLocation")
    public ArrayList<Bus> findAllBusByBoardingAndDestination(@Param("sLocation") String boardingLocation,
            @Param("eLocation") String destinationLocation);

}
