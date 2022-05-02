package com.app.BookingApp.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    public BusService(BusRespository busRespository, LocationRepository locationRepository,
            SeatRepository seatRepository) {
        this.busRespository = busRespository;
        this.locationRepository = locationRepository;
        this.seatRepository = seatRepository;
    }

    public ResponseEntity<Object> getBusDetails(Long id) {

        Optional<Bus> optionalBus = busRespository.findById(id);

        if (!optionalBus.isPresent()) {
            return new ResponseEntity<Object>("no such bus found", HttpStatus.CREATED);
        }

        return new ResponseEntity<Object>(optionalBus, HttpStatus.OK);
    }

    public ResponseEntity<Object> addBusDetails(Iterable<Bus> busDatas) {

        Iterator<Bus> iterator = busRespository.saveAll(busDatas).iterator();

        while (iterator.hasNext()) {
            Bus bus = iterator.next();
            int numberOfSeates = bus.getNumberOfSeats();
            for (int i = 1; i <= numberOfSeates; i++) {
                Seat s = new Seat();
                s.setBus(bus);
                s.setSeatNo(i);
                seatRepository.save(s);
            }
        }

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getAllLocations() {
        Map<String, Object> response = new HashMap<>();
        Iterable<Location> locations = locationRepository.findAll();
        Long numberOfLocations = locationRepository.count();

        response.put("count", numberOfLocations);
        response.put("results", locations);

        return new ResponseEntity<Object>(response, HttpStatus.OK);

    }

    public ResponseEntity<Object> addLocations(Iterable<String> locations) {
        Iterator<String> iterator = locations.iterator();

        while (iterator.hasNext()) {
            String location = iterator.next();
            Optional<Location> optionalLocation = locationRepository.findByLocation(location);

            if (!optionalLocation.isPresent()) {
                locationRepository.save(new Location(location.toLowerCase()));
            }
        }

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.CREATED);

    }

    public ResponseEntity<Object> findAvaliableSeats(Long busId) {
        Optional<Bus> optionalBus = busRespository.findById(busId);

        if (!optionalBus.isPresent()) {
            return new ResponseEntity<Object>("no such bus found", HttpStatus.OK);
        }
        
        Iterable<Seat> avaliableSeats = seatRepository.findByBusId(busId);
        int totalBusSeats = optionalBus.get().getNumberOfSeats();

        Map<String, Object> seatResponse = new HashMap<>();
        Map<Integer, Boolean> seatsStatus = new HashMap<>();
        Iterator<Seat> iterator = avaliableSeats.iterator();
        int avaliableSeatsCounts = 0;

        while (iterator.hasNext()) {
            Seat seat = iterator.next();

            if (seat.isIsAvaliable()) {
                avaliableSeatsCounts += 1;
            }

            seatsStatus.put(seat.getSeatNo(), seat.isIsAvaliable());
        }

        seatResponse.put("busId", busId);
        seatResponse.put("totalBusSeats", totalBusSeats);
        seatResponse.put("seatAvaliable", avaliableSeatsCounts);
        seatResponse.put("results", seatsStatus);

        return new ResponseEntity<Object>(seatResponse, HttpStatus.OK);

    }

    public ResponseEntity<Object> findBusesByLocation(String boardingLocation, String destinationLocation) {
        Optional<Location> optionalBoardingLocation = locationRepository.findByLocation(boardingLocation);
        Optional<Location> optionalDestinationLocation = locationRepository.findByLocation(destinationLocation);

        if ((!optionalBoardingLocation.isPresent()) && (!optionalDestinationLocation.isPresent())) {
            return new ResponseEntity<Object>("no services avaliable", HttpStatus.BAD_REQUEST);

        }

        Set<Bus> busesByStartLocation = busRespository.findAllByStartLocation(boardingLocation);
        Set<Bus> busesByEndLocation = busRespository.findAllByEndLocation(destinationLocation);

        Set<Bus> busesList = findCommonBuses(busesByStartLocation, busesByEndLocation);
        
        if(busesList.isEmpty()){
            return new ResponseEntity<Object>("no bus avaliable", HttpStatus.OK);
        }
        
        Map<String, Object> busResponse = new HashMap<>();

        busResponse.put("counts", busesList.size());
        busResponse.put("results", busesList);

        return new ResponseEntity<Object>(busResponse, HttpStatus.OK);

    }

    private Set<Bus> findCommonBuses(Set<Bus> busesByStartLocation, Set<Bus> busesByEndLocation) {
        Set<Bus> intersectionBuses = new HashSet<>(busesByStartLocation);
        intersectionBuses.retainAll(busesByEndLocation);

        return intersectionBuses;
    }

}
