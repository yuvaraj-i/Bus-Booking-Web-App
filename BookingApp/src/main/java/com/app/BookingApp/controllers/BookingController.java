package com.app.BookingApp.controllers;

import com.app.BookingApp.models.Bus;
import com.app.BookingApp.models.Location;
import com.app.BookingApp.services.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bus")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/find")
    public String searchBuses(@RequestBody Bus busSearchDetails){
        return bookingService.getBusDetails(busSearchDetails);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addNewBus(@RequestBody Iterable<Bus> busDatas){
        return bookingService.addBusDetails(busDatas);
    }

    @GetMapping("/locations")
    public ResponseEntity<Object> getAllDestination(){
        return bookingService.getAllLocations();
    }

    @PutMapping("/add/locations")
    public ResponseEntity<Object> addLocations(@RequestBody Iterable<Location> locations){
        return bookingService.addLocations(locations);
    }

}
