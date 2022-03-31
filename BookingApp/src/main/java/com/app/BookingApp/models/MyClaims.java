package com.app.BookingApp.models;

public class MyClaims {
    private Long id;
    private String mobileNumber;

    public MyClaims(Long id, String mobileNumber) {
        this.id = id;
        this.mobileNumber = mobileNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

}
