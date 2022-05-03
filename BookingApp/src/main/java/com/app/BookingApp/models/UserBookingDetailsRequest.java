package com.app.BookingApp.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBookingDetailsRequest {
    private String userName;
    private String email;
    private ArrayList<Passenger> passengers;
    private ArrayList<Integer> seatNumbers;
}
