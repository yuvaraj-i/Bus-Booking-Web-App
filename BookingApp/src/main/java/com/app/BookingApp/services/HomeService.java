package com.app.BookingApp.services;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.BookingApp.configuration.JwtTokenUtils;
import com.app.BookingApp.models.MyClaims;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.reposistory.MyUserResposistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtils jwtUtils;
    private MyUserResposistory userResposistory;

    @Autowired
    public HomeService(AuthenticationManager authenticationManager, JwtTokenUtils jwtUtils,
            MyUserResposistory userResposistory) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userResposistory = userResposistory;
    }

    public String authentication(MyUser user) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getMobileNumber(), user.getPassword()));

        } catch (Exception e) {
            throw new Exception("Invalid username or password");
        }

        // Optional<MyUser> userData =
        // userService.getUserByMobileNumber(user.getMobileNumber());
        // Long id = userData.get().getId();
        // String mobileNumber = userData.get().getMobileNumber();
        // UserDetails userDetails = userService
        // .loadUserByUsername(user.getName());
        String jwtToken = jwtUtils.generateToken(new MyClaims(user.getId(), user.getMobileNumber()));

        return jwtToken;
    }

    public String addNewUser(MyUser user) {
        Optional<MyUser> optionalEmail = userResposistory.findUserByEmailAddress(user.getEmailAddress());
        Optional<MyUser> optionalMobile = userResposistory.findUserByMobileNumber(user.getMobileNumber());

        if (!isNameValid(user.getName().trim())) {
            throw new IllegalArgumentException("name formate error");
        }

        if (!isMobileNumberValid(user.getMobileNumber().trim())) {
            throw new IllegalArgumentException("invalid mobile number");
        }

        if (!isEmailValid(user.getEmailAddress().trim())) {
            throw new IllegalArgumentException("invalid Email number");
        }

        if (!isValidPassword(user.getPassword().trim())) {
            throw new IllegalArgumentException("invalid password number");
        }

        if (optionalEmail.isPresent()) {
            throw new IllegalArgumentException("Email Already Present");
        }

        if (optionalMobile.isPresent()) {
            throw new IllegalArgumentException("Mobile Number Already Present");
        }

        Long id = userResposistory.save(user).getId();
        return jwtUtils.generateToken(new MyClaims(id, user.getMobileNumber()));
    }

    private boolean isEmailValid(String email) {
        // System.out.println("im email");
        if (email == null) {
            return false;
        }

        // String regex = "^(.+)@(.+)$";
        // Pattern pattern = Pattern.compile(regex);
        // Matcher matcher = pattern.matcher(email);
        return true;

    }

    private boolean isMobileNumberValid(String number) {
        // System.out.println("im mobile");
        if (number == null) {
            return false;
        }

        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    private boolean isNameValid(String name) {
        // System.out.println("im name check");
        if (name == null) {
            return false;
        }
        // String regex = "\\d";
        // Pattern pattern = Pattern.compile(regex);
        // Matcher matcher = pattern.matcher(name);
        // return matcher.matches();
        return (name.length() > 0) && (name.length() <= 30);
    }

    private boolean isValidPassword(String password) {
        // System.out.println("im password check");
        if (password == null) {
            return false;
        }
        return password.length() > 8;
    }

}
