package com.app.BookingApp.models;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String busName;
    private int numberOfSeats;
    private String type;
    private String startLocation;
    private String endLocation;
    private String boardingPoint;
    private String dropingPoint;
    private LocalTime startTime;
    private LocalTime endTime;
    private String numberPlateDeatails;
    private boolean isAvaliable = true;
    private boolean isDestinationReached = false;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusName() {
        return this.busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public int getNumberOfSeats() {
        return this.numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return this.endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getBoardingPoint() {
        return this.boardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public String getDropingPoint() {
        return this.dropingPoint;
    }

    public void setDropingPoint(String dropingPoint) {
        this.dropingPoint = dropingPoint;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isIsAvaliable() {
        return this.isAvaliable;
    }

    public boolean getIsAvaliable() {
        return this.isAvaliable;
    }

    public void setIsAvaliable(boolean isAvaliable) {
        this.isAvaliable = isAvaliable;
    }


    public boolean isIsDestinationReached() {
        return this.isDestinationReached;
    }

    public boolean getIsDestinationReached() {
        return this.isDestinationReached;
    }

    public void setIsDestinationReached(boolean isDestinationReached) {
        this.isDestinationReached = isDestinationReached;
    }


    public String getNumberPlateDetails() {
        return this.numberPlateDeatails;
    }

    public void setNumberPlateDetails(String numberPlateDeatails) {
        this.numberPlateDeatails = numberPlateDeatails;
    }


}
