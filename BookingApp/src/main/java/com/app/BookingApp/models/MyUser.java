package com.app.BookingApp.models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String mobileNumber;
    private String emailAddress;
    private LocalDate dateOfBirth;
    private int age;
    private String password;
    private boolean isEnabled = true;

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.setAge(dateOfBirth);
    }

    private void setAge(LocalDate age) {
        LocalDate currentDate = LocalDate.now();
        this.age = currentDate.getYear() - age.getYear();
    }


}
