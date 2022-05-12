package com.app.BookingApp.models;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.Data;

@Data
public class BookingResponse {
    private String busName;
    private String boardingLocation;
    private String destinationLocation;
    private LocalDate bookingDate;
    private LocalDate travelingDate;
    private ArrayList<Passenger> passengers;
    private ArrayList<Integer> seatNumbers;
    private int charges;
    private Long ticketId;

}
