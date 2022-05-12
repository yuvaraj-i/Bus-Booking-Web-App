package com.app.BookingApp.models;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String busName;
    private int numberOfSeats;
    private String type;
    private String startLocation;
    private String endLocation;
    private String pickupPoint;
    private String dropingPoint;
    private LocalTime startingTime;
    private LocalTime reachingTime;
    private int seatPrice;
    private String numberPlateDeatails;
    private boolean isAvaliable = true;
    private boolean isDestinationReached = false;

}
