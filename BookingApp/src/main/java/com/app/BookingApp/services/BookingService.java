package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import com.app.BookingApp.models.Bus;
import com.app.BookingApp.models.Location;
import com.app.BookingApp.repository.BusRespository;
import com.app.BookingApp.repository.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class BookingService {

    private BusRespository busRespository;
    private LocationRepository locationRepository;

    @Autowired
    public BookingService(BusRespository busRespository, LocationRepository locationRepository) {
        this.busRespository = busRespository;
        this.locationRepository = locationRepository;
    }

    public String getBusDetails(Bus details) {
        // Optional<Bus> optionalBusDetails = bookingResposistory.findAllBusBystartPlaceAndendPlace(
        //     details.getStartPlace(), details.getEndPlace());


        // if(!optionalBusDetails.isPresent()){
        //     // "return no buses avaliable";
        // }

        // Iterable<Bus> allBueses = optionalBusDetails.get();

        return "hi";
    }

    public ResponseEntity<Object> addBusDetails(Iterable<Bus> busDatas) {
        busRespository.saveAll(busDatas);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }

    public ResponseEntity<Object> getAllLocations() {
        Map<String, Object> map = new HashMap<>();
        Iterable<Location> locations = locationRepository.findAll();
        Long numberOfLocations = locationRepository.count();

        map.put("count", numberOfLocations);
        map.put("results", locations);

        return new ResponseEntity<Object>(map, HttpStatus.OK);

    }

    public ResponseEntity<Object> addLocations(Iterable<Location> locations) {
        Iterator<Location> iterator = locations.iterator();

        while(iterator.hasNext()){
            Location location = iterator.next();
            Optional<Location> optionalLocation = locationRepository.findByPlace(location.getPlace());

            if(!optionalLocation.isPresent()){
                locationRepository.save(location);
            }
        }

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);

    }
    
}
