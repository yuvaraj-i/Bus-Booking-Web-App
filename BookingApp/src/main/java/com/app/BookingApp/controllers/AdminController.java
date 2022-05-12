package com.app.BookingApp.controllers;

import com.app.BookingApp.models.Bus;
import com.app.BookingApp.models.MyUser;
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
    public Iterable<MyUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("update/{id}")
    public void deleteExtistingUser(@PathVariable("id") Long userId) {
        userService.deleteExtistingUser(userId);
    }

}