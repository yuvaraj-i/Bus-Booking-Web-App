package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.app.BookingApp.configuration.JwtTokenUtils;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.Roles;
import com.app.BookingApp.models.UserProfile;
import com.app.BookingApp.repository.MyUserRespository;
import com.app.BookingApp.repository.RolesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

    private MyUserRespository userRespository;
    private RolesRepository rolesRepository;
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    public MyUserService(MyUserRespository userRespository, RolesRepository rolesRepository,
            JwtTokenUtils jwtTokenUtils) {
        this.userRespository = userRespository;
        this.rolesRepository = rolesRepository;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return null;
        }

        MyUser user = optionalUser.get();

        // return new User(user.getMobileNumber(), user.getPassword(), new
        // ArrayList<>());

        Iterable<Roles> userRoles = rolesRepository.findByUserId(user.getId());
        MyUserDetails userWithAuthorithy = new MyUserDetails(user, userRoles);

        // System.out.println(userWithAuthorithy.getUsername());
        // System.out.println(userWithAuthorithy.getUsername());

        return userWithAuthorithy;
    }

    public List<UserProfile> getAllUsers() {
        List<UserProfile> allUserList = new ArrayList<>();
        Iterable<MyUser> allUserDatasList = userRespository.findAll();
        Iterator<MyUser> userIterator = allUserDatasList.iterator();

        while (userIterator.hasNext()) {
            MyUser userData = userIterator.next();
            UserProfile userResponse = new UserProfile();

            userResponse.setName(userData.getName());
            userResponse.setAge(userData.getAge());
            userResponse.setDateOfBirth(userData.getDateOfBirth());
            userResponse.setEmailAddress(userData.getEmailAddress());
            userResponse.setMobileNumber(userData.getMobileNumber());

            allUserList.add(userResponse);
        }

        return allUserList;
    }

    public ResponseEntity<Object> getUserById(HttpServletRequest request) {
        String mobileNumber = getUserId(request);
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<Object>("User Not Found", HttpStatus.BAD_REQUEST);

        }

        MyUser user = optionalUser.get();
        UserProfile userProfile = new UserProfile();

        userProfile.setDateOfBirth(user.getDateOfBirth());
        userProfile.setEmailAddress(user.getEmailAddress());
        userProfile.setMobileNumber(user.getMobileNumber());
        userProfile.setName(user.getName());
        userProfile.setAge(user.getAge());

        return new ResponseEntity<Object>(userProfile, HttpStatus.OK);

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

    public String getUserId(HttpServletRequest request) {
        String jwtToken = "";

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization_1")) {
                    jwtToken = cookie.getValue();
                }
            }
        }

        String mobileNumber = jwtTokenUtils.getUserMobileNumber(jwtToken);

        return mobileNumber;
    }

    public ResponseEntity<Object> getUserRoles(HttpServletRequest request) {

        ArrayList<String> rolesList = new ArrayList<>();

        String mobileNumber = getUserId(request);
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<Object>("User found", HttpStatus.BAD_REQUEST);
        }

        Long userId = optionalUser.get().getId();
        Iterable<Roles> roles = rolesRepository.findByUserId(userId);
        Iterator<Roles> rolesIterator = roles.iterator();

        while (rolesIterator.hasNext()) {
            Roles userRoles = rolesIterator.next();
            rolesList.add(userRoles.getRole());
        }

        return new ResponseEntity<Object>(rolesList, HttpStatus.OK);
    }

}
