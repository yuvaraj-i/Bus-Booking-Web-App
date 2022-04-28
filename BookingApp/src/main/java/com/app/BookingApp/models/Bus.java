package com.app.BookingApp.models;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    // private String type;
    private Long numberOfSeats;
    // @OneToMany
    private Long startPlaceId;
    // @OneToMany
    private Long endPlaceId;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAvaliable = true;


    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Long getNumberOfSeats() {
        return this.numberOfSeats;
    }

    // public String getType() {
    //     return this.type;
    // }

    public Long getStartPlaceId() {
        return this.startPlaceId;
    }

    public Long getEndPlaceId() {
        return this.endPlaceId;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public boolean getIsAvaliable() {
        return this.isAvaliable;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfSeats(Long numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    // public void setType(String type) {
    //     this.type = type;
    // }

    public void setStartPlaceId(Long startPlaceId) {
        this.startPlaceId = startPlaceId;
    }

    public void setEndPlace(Long endPlaceId) {
        this.endPlaceId = endPlaceId;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    public void setIsAvaliable() {
        if(this.numberOfSeats == 0){
            this.isAvaliable = false;
        }
    }

}
