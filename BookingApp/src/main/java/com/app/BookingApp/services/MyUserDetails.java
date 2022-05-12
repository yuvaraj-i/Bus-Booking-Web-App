package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.Roles;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetails {

    private Iterable<Roles> roles;
    private MyUser user;
    // private RolesRepository rolesRepository;
    
    // @Autowired
    // public MyUserDetails(RolesRepository rolesRepository) {
    //     this.rolesRepository = rolesRepository;
    // 
    public MyUserDetails () {}


    public MyUserDetails(MyUser user, Iterable<Roles> roles) {
        this.user = user;
        this.roles = roles;
        
        // Iterable<Roles> userRoles = rolesRepository.findByUserId(user.getId());
        // System.out.println(userRoles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        Iterator<Roles> rolesIterator = roles.iterator();

        while (rolesIterator.hasNext()) {
            Roles userRolesValues = rolesIterator.next();
            authList.add(new SimpleGrantedAuthority(userRolesValues.getRole()));
        }

        return authList;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getMobileNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
