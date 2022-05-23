package com.app.BookingApp.models;

import java.util.ArrayList;

import lombok.Data;

@Data
public class MyClaims {
    private String mobileNumber;
    private ArrayList<String> roles;

    public MyClaims(String mobileNumber, ArrayList<String> roles) {
        this.mobileNumber = mobileNumber;
        this.roles = roles;
    }

}
