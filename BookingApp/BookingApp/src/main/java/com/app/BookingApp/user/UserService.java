package com.app.BookingApp.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private UserResposistory userResposistory;

    @Autowired
    public UserService(UserResposistory userResposistory) {
        this.userResposistory = userResposistory;
    }

    public Iterable<User> getAllUsers() {
        return userResposistory.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userResposistory.findById(id);
    }

    public void addNewUser(User user) {
        Optional<User> optionalEmail = userResposistory.findUserByEmailAddress(user.getEmailAddress());
        Optional<User> optionalMobile = userResposistory.findUserByMobileNumber(user.getMobileNumber());

        if (optionalEmail.isPresent()) {
            throw new IllegalStateException("Email Already Present");
        }

        if (optionalMobile.isPresent()) {
            throw new IllegalStateException("Mobile Number Already Present");
        }

        userResposistory.save(user);
    }

    public void deleteExtistingUser(Long id) {
        boolean status = userResposistory.existsById(id);

        if (!status) {
            throw new IllegalStateException("user with " + id + " not found!");
        }

        userResposistory.deleteById(id);
    }

}
