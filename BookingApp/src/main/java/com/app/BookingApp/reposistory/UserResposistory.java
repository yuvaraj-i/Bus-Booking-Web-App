package com.app.BookingApp.reposistory;

import java.util.Optional;

import com.app.BookingApp.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResposistory extends CrudRepository<User, Long> {

    public Optional<User> findUserByEmailAddress(String emailAddress);

    public Optional<User> findUserByMobileNumber(String mobileNumber);

}
