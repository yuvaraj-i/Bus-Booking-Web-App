package com.app.BookingApp.model.user.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.app.BookingApp.model.User;

@Entity
@Table
public class UserRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;

    @OneToOne
    private User userId;

    public Long getId() {
        return this.id;
    }

    public User getUserId() {
        return this.userId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
