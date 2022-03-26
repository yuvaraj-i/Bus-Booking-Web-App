package com.app.BookingApp.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserResposistory extends CrudRepository<User, Long> {

    public Optional<User> findUserByEmailAddress(String emailAddress);

    public Optional<User> findUserByMobileNumber(String mobileNumber);

}
