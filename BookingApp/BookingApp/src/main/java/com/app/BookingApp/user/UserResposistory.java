package com.app.BookingApp.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResposistory extends CrudRepository<User, Long> {
    
}
