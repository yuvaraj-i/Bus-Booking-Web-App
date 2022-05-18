package com.app.BookingApp.models;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {
    private String name;
    private String mobileNumber;
    private String emailAddress;
    private LocalDate dateOfBirth;
    private int age; 
    private ArrayList<String> roles;
    private boolean isEnable;
}
