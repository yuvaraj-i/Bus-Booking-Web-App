package com.app.BookingApp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "busId", referencedColumnName = "id")
    private Bus bus;
    private int SeatNo;
    private Boolean isAvaliable = true;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public int getSeatNo() {
        return this.SeatNo;
    }

    public void setSeatNo(int SeatNo) {
        this.SeatNo = SeatNo;
    }

    public Boolean isIsAvaliable() {
        return this.isAvaliable;
    }

    public Boolean getIsAvaliable() {
        return this.isAvaliable;
    }

    public void setIsAvaliable(Boolean isAvaliable) {
        this.isAvaliable = isAvaliable;
    }

    
}
