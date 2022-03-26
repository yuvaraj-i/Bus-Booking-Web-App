package com.app.BookingApp.user;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String name;
    private String mobileNumber;
    private String emailAddress;
    private LocalDate dateOfBirth;
    private int age;

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public int getAge() {
        return this.age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.setAge(dateOfBirth);
    }

    private void setAge(LocalDate age) {
        LocalDate currentDate = LocalDate.now();
        this.age = currentDate.getYear() - age.getYear();
    }

    public void setAge(int age){
        this.age = age;
    }

}
