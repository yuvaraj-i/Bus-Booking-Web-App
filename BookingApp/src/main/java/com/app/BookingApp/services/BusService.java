package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.app.BookingApp.models.Bus;
import com.app.BookingApp.models.Location;
import com.app.BookingApp.models.Orders;
import com.app.BookingApp.models.Passenger;
import com.app.BookingApp.models.Seat;
import com.app.BookingApp.models.SeatResponse;
import com.app.BookingApp.models.Ticket;
import com.app.BookingApp.repository.BusRespository;
import com.app.BookingApp.repository.LocationRepository;
import com.app.BookingApp.repository.OrderReposistory;
import com.app.BookingApp.repository.PassengerReposistory;
import com.app.BookingApp.repository.SeatRepository;
import com.app.BookingApp.repository.TicketReposistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BusService {

    private BusRespository busRespository;
    private LocationRepository locationRepository;
    private SeatRepository seatRepository;
    private TicketReposistory ticketReposistory;
    private OrderReposistory orderReposistory;
    private PassengerReposistory passengerReposistory;

    @Autowired
    public BusService(BusRespository busRespository, LocationRepository locationRepository,
            SeatRepository seatRepository, TicketReposistory ticketReposistory, OrderReposistory orderReposistory,
            PassengerReposistory passengerReposistory) {
        this.busRespository = busRespository;
        this.locationRepository = locationRepository;
        this.seatRepository = seatRepository;
        this.ticketReposistory = ticketReposistory;
        this.orderReposistory = orderReposistory;
        this.passengerReposistory = passengerReposistory;
    }

    public ResponseEntity<Object> getBusDetails(Long id) {

        Optional<Bus> optionalBus = busRespository.findById(id);

        if (!optionalBus.isPresent()) {
            return new ResponseEntity<Object>("no such bus found", HttpStatus.CREATED);
        }

        return new ResponseEntity<Object>(optionalBus, HttpStatus.OK);
    }

    public ResponseEntity<Object> addBusDetails(Iterable<Bus> busDatas) {

        Iterator<Bus> busDatasIterator = busDatas.iterator();

        while (busDatasIterator.hasNext()) {
            Bus busDetails = busDatasIterator.next();

            Optional<Bus> extistingBusOptional = busRespository
                    .findBynumberPlateDeatails(busDetails.getNumberPlateDeatails());

            if (!extistingBusOptional.isPresent()) {
                Bus savedBus = busRespository.save(busDetails);
                int numberOfSeates = busDetails.getNumberOfSeats();
                for (int i = 1; i <= numberOfSeates; i++) {
                    Seat s = new Seat();
                    s.setBus(savedBus);
                    s.setSeatNo(i);
                    seatRepository.save(s);
                }
            }
        }

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getAllLocations() {
        Map<String, Object> response = new HashMap<>();
        ArrayList<String> locationsList = new ArrayList<>();

        Iterable<Location> locations = locationRepository.findAll();
        Long numberOfLocations = locationRepository.count();
        Iterator<Location> locationsIter = locations.iterator();

        while (locationsIter.hasNext()) {
            Location locationName = locationsIter.next();
            locationsList.add(locationName.getLocation());
        }

        response.put("count", numberOfLocations);
        response.put("results", locationsList);

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
        ArrayList<SeatResponse> seatsStatus = new ArrayList<>();
        Iterator<Seat> iterator = avaliableSeats.iterator();
        int avaliableSeatsCounts = 0;

        while (iterator.hasNext()) {
            Seat seat = iterator.next();

            if (seat.getIsAvaliable()) {
                avaliableSeatsCounts += 1;
            }

            SeatResponse seatDetails = new SeatResponse();
            seatDetails.setAvaliable(seat.getIsAvaliable());
            seatDetails.setSeatNumber(seat.getSeatNo());
            seatsStatus.add(seatDetails);
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

        ArrayList<Bus> busesList = busRespository.findAllBusByBoardingAndDestination(boardingLocation,
                destinationLocation);

        if (busesList.size() == 0) {
            return new ResponseEntity<Object>("no bus avaliable", HttpStatus.OK);
        }

        Map<String, Object> busResponse = new HashMap<>();

        busResponse.put("counts", busesList.size());
        busResponse.put("results", busesList);

        return new ResponseEntity<Object>(busResponse, HttpStatus.OK);

    }

    public int calculateBusCharges(Bus bus, int numberOfSeats) {
        int seatPrice = bus.getSeatPrice();

        return numberOfSeats * seatPrice;
    }

    public ResponseEntity<Object> updateBusData(Bus bus) {
        busRespository.save(bus);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }

    public Iterable<Bus> getAllbuses() {
        Iterable<Bus> buses = busRespository.findAll();

        return buses;
    }

    @Transactional
    public ResponseEntity<Object> deleteLocation(String location) {
        Optional<Location> optionalLocation = locationRepository.findByLocation(location);

        if (!optionalLocation.isPresent()) {
            return new ResponseEntity<Object>("Location not found", HttpStatus.BAD_REQUEST);
        }

        locationRepository.delete(optionalLocation.get());

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> deleteBusData(String busRegisterNumber) {
        Optional<Bus> optionalBus = busRespository.findBynumberPlateDeatails(busRegisterNumber);

        if (!optionalBus.isPresent()) {
            return new ResponseEntity<Object>("Bus not found", HttpStatus.BAD_REQUEST);
        }

        Bus bus = optionalBus.get();
        Iterable<Seat> busSeatsList = seatRepository.findByBusId(bus.getId());
        seatRepository.deleteAll(busSeatsList);
        
        Iterable<Ticket> tickets = ticketReposistory.findByBusId(bus.getId());
        Iterator<Ticket> ticketsIterator = tickets.iterator();

        while (ticketsIterator.hasNext()) {
            Ticket userBookedTickets = ticketsIterator.next();

            Iterable<Orders> totalTickets = orderReposistory.findAllByTicketId(userBookedTickets.getId());
            Iterable<Passenger> passenger = passengerReposistory.findByTicketId(userBookedTickets.getId());

            orderReposistory.deleteAll(totalTickets);
            passengerReposistory.deleteAll(passenger);
        }

        ticketReposistory.deleteAll(tickets);
        busRespository.delete(bus);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }

}
