package com.app.BookingApp.controllers;

import com.app.BookingApp.models.UserBookingDetailsRequest;
import com.app.BookingApp.services.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
public class BookingController {
    
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bus/ticket/{busId}")
    public ResponseEntity<Object> bookTicket(@PathVariable("busId") Long busId, @RequestBody UserBookingDetailsRequest userBookingDetails){
        return bookingService.bookTicketForUser(userBookingDetails, busId);
    }

}
