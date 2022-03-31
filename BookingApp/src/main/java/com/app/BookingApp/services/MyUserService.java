package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.Optional;
import javax.transaction.Transactional;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.reposistory.MyUserResposistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

    private MyUserResposistory userResposistory;

    @Autowired
    public MyUserService(MyUserResposistory userResposistory) {
        this.userResposistory = userResposistory;
    }

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        MyUser user = userResposistory.findUserByMobileNumber(mobileNumber).get();

        return new User(user.getMobileNumber(), user.getPassword(), new ArrayList<>());
    }

    public Iterable<MyUser> getAllUsers() {
        return userResposistory.findAll();
    }

    public Optional<MyUser> getUserById(Long id) {
        return userResposistory.findById(id);
    }

    public Optional<MyUser> getUserByMobileNumber(String mobileNumber) {
        return userResposistory.findUserByMobileNumber(mobileNumber);
    }

    // public String checkUserLogin(MyUser details) {
    // if (details.getMobileNumber() == null || details.getMobileNumber() == null) {
    // throw new IllegalArgumentException("no input found");
    // }
    // Optional<MyUser> user =
    // userResposistory.findUserByMobileNumber(details.getMobileNumber());

    // if (!user.isPresent()) {
    // throw new IllegalArgumentException("User Not found");
    // }

    // String userPassword =
    // userResposistory.findUserByMobileNumber(details.getMobileNumber()).get().getPassword();

    // if (!userPassword.equals(details.getPassword())) {
    // throw new IllegalArgumentException("Worng password");
    // }

    // Long id =
    // userResposistory.findUserByMobileNumber(details.getMobileNumber()).get().getId();

    // return jawt.generateToken(new MyClaims(id, details.getMobileNumber()));

    // }

    public void deleteExtistingUser(Long id) {
        boolean iSUserExist = userResposistory.existsById(id);

        if (!iSUserExist) {
            throw new IllegalStateException("user with " + id + " not found!");
        }

        userResposistory.deleteById(id);
    }

    @Transactional
    public void updateUser(Long id, String name, String emailAddress) {
        Optional<MyUser> user = userResposistory.findById(id);

        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

    }

}
