package com.app.BookingApp.repository;

import java.util.Optional;

import com.app.BookingApp.models.Location;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long>{

    public Optional<Location> findByLocation(String location);
    
}
