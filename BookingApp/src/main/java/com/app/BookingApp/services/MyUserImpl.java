// package com.app.BookingApp.services;

// import java.util.ArrayList;
// import java.util.Collection;
// import java.util.Iterator;
// import java.util.List;

// import com.app.BookingApp.models.MyUser;
// import com.app.BookingApp.models.Roles;
// import com.app.BookingApp.repository.RolesRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Service;

// @Service
// public class MyUserImpl implements UserDetails{

//     @Autowired
//     private RolesRepository rolesRepository;
//     private MyUser user;


//     public MyUserImpl() { }


//     public MyUserImpl (MyUser user) {
//         this.user = user;
//     }


//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//         List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
//         Iterable<Roles> userRoles = rolesRepository.findByUserId(user.getId());
//         Iterator<Roles> userIterator = userRoles.iterator();

//         while(userIterator.hasNext()) {
//             Roles userRolesValues = userIterator.next();
//             authList.add(new SimpleGrantedAuthority(userRolesValues.getRole()));
//         }
        
//         return authList;
//     }

//     @Override
//     public String getPassword() {
//         return this.user.getPassword();
//     }

//     @Override
//     public String getUsername() {
//         return this.user.getMobileNumber();
//     }

//     @Override
//     public boolean isAccountNonExpired() {
//         return true;
//     }

//     @Override
//     public boolean isAccountNonLocked() {
//         return true;
//     }

//     @Override
//     public boolean isCredentialsNonExpired() {
//         return true;
//     }

//     @Override
//     public boolean isEnabled() {
//         return true;
//     }
    
// }
