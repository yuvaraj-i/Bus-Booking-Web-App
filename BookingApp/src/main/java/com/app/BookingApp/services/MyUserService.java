package com.app.BookingApp.services;

import java.util.Optional;

import javax.transaction.Transactional;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.Roles;
import com.app.BookingApp.repository.MyUserRespository;
import com.app.BookingApp.repository.RolesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

    private MyUserRespository userRespository;
    private RolesRepository rolesRepository;

    @Autowired
    public MyUserService(MyUserRespository userRespository, RolesRepository rolesRepository) {
        this.userRespository = userRespository;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return null;
        }

        MyUser user = optionalUser.get();

        
        // return new User(user.getMobileNumber(), user.getPassword(), new ArrayList<>());
        
        Iterable<Roles> userRoles = rolesRepository.findByUserId(user.getId());
        MyUserDetails userWithAuthorithy = new MyUserDetails(user, userRoles);
        
        // System.out.println(userWithAuthorithy.getUsername());
        // System.out.println(userWithAuthorithy.getUsername());

        return userWithAuthorithy;
    }

    public Iterable<MyUser> getAllUsers() {
        return userRespository.findAll();
    }

    public Optional<MyUser> getUserById(Long id) {
        return userRespository.findById(id);
    }

    public Optional<MyUser> getUserByMobileNumber(String mobileNumber) {
        return userRespository.findUserByMobileNumber(mobileNumber);
    }

    public void deleteExtistingUser(Long id) {
        boolean iSUserExist = userRespository.existsById(id);

        if (!iSUserExist) {
            throw new IllegalStateException("user with " + id + " not found!");
        }

        userRespository.deleteById(id);
    }

    @Transactional
    public void updateUser(Long id, String name, String emailAddress) {
        Optional<MyUser> user = userRespository.findById(id);

        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

    }

}
