package com.app.BookingApp.controllers;

import java.util.List;

import com.app.BookingApp.models.Bus;
import com.app.BookingApp.models.UserProfile;
import com.app.BookingApp.services.BusService;
import com.app.BookingApp.services.MyUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private BusService busService;
    private MyUserService userService;

    @Autowired
    public AdminController(BusService busService, MyUserService userService) {
        this.busService = busService;
        this.userService = userService;
    }

    @PutMapping("/add/locations")
    public ResponseEntity<Object> addLocations(@RequestBody Iterable<String> locations) {
        return busService.addLocations(locations);
    }

    @PostMapping("/add/bus")
    public ResponseEntity<Object> addNewBus(@RequestBody Iterable<Bus> busDatas) {
        return busService.addBusDetails(busDatas);
    }

    @GetMapping("/all")
    public List<UserProfile> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/bus/update")
    public ResponseEntity<Object> updateBusDetails(@RequestBody Bus bus) {
        return busService.updateBusData(bus);
    }

    @GetMapping("/bus/all")
    public Iterable<Bus> getAllBuses() {
        return busService.getAllbuses();
    }

    @PutMapping("/user/disable/{id}")
    public ResponseEntity<Object> disableUser(@PathVariable("id") String mobileNumber) {
        return userService.setUserInActive(mobileNumber);
    }

    @PutMapping("/user/enable/{id}")
    public ResponseEntity<Object> enableUser(@PathVariable("id") String mobileNumber) {
        return userService.setUserActive(mobileNumber);
    }

    @DeleteMapping("/delete/location/{id}")
    public ResponseEntity<Object> deleteLocation(@PathVariable("id") String location) {
        return busService.deleteLocation(location);

    }

    // @DeleteMapping("/delete/bus/{id}")
    // public ResponseEntity<Object> deleteBusData(@PathVariable("id") String busRegisterNumber){
    //     return busService.deleteBusData(busRegisterNumber);
    // }

}
