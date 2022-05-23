package com.app.BookingApp.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BookingResponse {
    private String busName;
    private String boardingLocation;
    private String destinationLocation;
    private String pickupPoint;
    private String dropingPoint;
    private LocalDate bookingDate;
    private LocalDate travelingDate;
    private List<Passenger> passengers;
    private ArrayList<Integer> seatNumbers;
    private int charges;
    private Long ticketId;
    private String busType;

}
