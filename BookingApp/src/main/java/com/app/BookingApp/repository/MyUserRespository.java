package com.app.BookingApp.repository;

import java.util.Optional;

import com.app.BookingApp.models.MyUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRespository extends CrudRepository<MyUser, Long> {

    public Optional<MyUser> findUserByEmailAddress(String emailAddress);

    public Optional<MyUser> findUserByMobileNumber(String mobileNumber);

}
