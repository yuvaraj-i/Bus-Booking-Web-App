package com.app.BookingApp.models;

import lombok.Data;

@Data
public class MyClaims {
    private Long id;
    private String mobileNumber;

    public MyClaims(Long id, String mobileNumber) {
        this.id = id;
        this.mobileNumber = mobileNumber;
    }

}
