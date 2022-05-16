package com.app.BookingApp.controllers;

import javax.servlet.http.HttpServletRequest;

import com.app.BookingApp.models.UserBookingDetailsRequest;
import com.app.BookingApp.services.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/details")
    public ResponseEntity<Object> userBookingHistory(HttpServletRequest request) {
        return bookingService.getUserBookingDetils(request);
    }

}
