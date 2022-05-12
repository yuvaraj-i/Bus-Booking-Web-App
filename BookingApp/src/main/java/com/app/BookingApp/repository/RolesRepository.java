package com.app.BookingApp.repository;

import com.app.BookingApp.models.Roles;

import org.springframework.data.repository.CrudRepository;

public interface RolesRepository extends CrudRepository<Roles, Long>{

    public Iterable<Roles> findByUserId(Long userId);
    
}
