package com.app.BookingApp.controllers;

import com.app.BookingApp.models.Bus;
import com.app.BookingApp.services.BusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bus")
public class BusController {

    private BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchBuses(@RequestParam("from") String boardingLocation,
            @RequestParam("to") String destinationLocation) {
        return busService.findBusesByLocation(boardingLocation, destinationLocation);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addNewBus(@RequestBody Iterable<Bus> busDatas) {
        return busService.addBusDetails(busDatas);
    }

    @GetMapping("/locations")
    public ResponseEntity<Object> getAllDestination() {
        return busService.getAllLocations();
    }

    @PutMapping("/add/locations")
    public ResponseEntity<Object> addLocations(@RequestBody Iterable<String> locations) {
        return busService.addLocations(locations);
    }

    @GetMapping("/{busId}")
    public ResponseEntity<Object> getBusDetailsById(@PathVariable("busId") Long id) {
        return busService.getBusDetails(id);
    }

    @GetMapping("seats/test/{busId}")
    public ResponseEntity<Object> test(@PathVariable("busId") Long busId) {
        return busService.findAvaliableSeats(busId);
    }

}
