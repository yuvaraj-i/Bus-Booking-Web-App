package com.app.BookingApp.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // private ArrayList<String> passenger;
    private Long userId;
    private Long busId;
    private String boardingPoint;
    private String droppingPoint;
    private LocalTime boardingTime;
    private LocalTime departureTime;
    private LocalDate dropingDate;
    private int busCharges;
    // private ArrayList<Integer> seatNumbers;

}
