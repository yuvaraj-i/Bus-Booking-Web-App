package com.app.BookingApp.models;

import lombok.Data;

@Data
public class MyClaims {
    private String mobileNumber;

    public MyClaims(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
