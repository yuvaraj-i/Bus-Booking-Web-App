package com.app.BookingApp.repository;

import com.app.BookingApp.models.Orders;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderReposistory extends CrudRepository<Orders, Long>{
    
}
