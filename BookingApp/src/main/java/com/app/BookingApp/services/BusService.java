package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.app.BookingApp.models.Bus;
import com.app.BookingApp.models.Location;
import com.app.BookingApp.models.Seat;
import com.app.BookingApp.repository.BusRespository;
import com.app.BookingApp.repository.LocationRepository;
import com.app.BookingApp.repository.SeatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BusService {

    private BusRespository busRespository;
    private LocationRepository locationRepository;
    private SeatRepository seatRepository;

    @Autowired
    public BusService(BusRespository busRespository, LocationRepository locationRepository, SeatRepository seatRepository) {
        this.busRespository = busRespository;
        this.locationRepository = locationRepository;
        this.seatRepository = seatRepository;
    }

    public Bus getBusDetails(Long id) {
        /*
        Optional<Bus> optionalBusDetails =
        bookingResposistory.findAllBusBystartPlaceAndendPlace(
        details.getStartPlace(), details.getEndPlace());

        if(!optionalBusDetails.isPresent()){
        // "return no buses avaliable";
        }

        Iterable<Bus> allBueses = optionalBusDetails.get();
        */
        Bus bus = busRespository.findById(id).get();

        return bus;
    }

    public ResponseEntity<Object> addBusDetails(Iterable<Bus> busDatas) {
        /*
        Location location = new Location();
        location.setPlace("coimbatore");
        ArrayList<Long> locations = new ArrayList<>();

        Long locationId = locationRepository.save(location).getId();
        locations.add(locationId);

        ArrayList<Long> seats = new ArrayList<>();

        for (int i = 0; i <= 4; i++){
            Seat seat = new Seat();
            seat.setSeatNo(i);
            Long id = seatRepository.save(seat).getId();
            seats.add(id);
        }

        Bus bus = new Bus();
        bus.setName("orange travels");
        bus.setNumberOfSeats(35);
        bus.setType("sleeper");
        bus.setSeatsId(seats);
        bus.setStartPlacesId(seats);
        bus.setEndPlacesId(locations);
        
        busRespository.save(bus);
        Iterable<Bus> allBuses = busRespository.findAll();

        Optional<Location> loca = locationRepository.findByPlace("coimbatore");
        
        if(!loca.isPresent()){
            return new ResponseEntity<Object>("No loc found", HttpStatus.OK);
            
        }
        Long val = loca.get().getId();
        Iterable<Bus> busVal = busRespository.findAllByStartPlacesId(val);
        */

        return new ResponseEntity<Object>("", HttpStatus.OK);
    }

    public ResponseEntity<Object> getAllLocations() {
        Map<String, Object> map = new HashMap<>();
        Iterable<Location> locations = locationRepository.findAll();
        Long numberOfLocations = locationRepository.count();

        map.put("count", numberOfLocations);
        map.put("results", locations);

        return new ResponseEntity<Object>(map, HttpStatus.OK);

    }

    public ResponseEntity<Object> addLocations(Iterable<String> locations) {
        Iterator<String> iterator = locations.iterator();

        while (iterator.hasNext()) {
            String location = iterator.next();
            Optional<Location> optionalLocation = locationRepository.findByLocation(location);

            if (!optionalLocation.isPresent()) {
                locationRepository.save(new Location(location));
            }
        }

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.CREATED);

    }

}
