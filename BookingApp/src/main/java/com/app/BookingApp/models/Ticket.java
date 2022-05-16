package com.app.BookingApp.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "id")
    private List<Passenger> passenger = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "busId", referencedColumnName = "id")
    private Bus bus;
    private int busCharges;
    private ArrayList<Integer> seatNumbers;
    private LocalDate bookedDate;
    private LocalDate travellingDate;

}
