package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.BookingApp.configuration.JwtTokenUtils;
import com.app.BookingApp.configuration.RefreshTokenUtils;
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
    private RefreshTokenUtils refreshUtils;

    @Autowired
    public HomeService(AuthenticationManager authenticationManager,
            JwtTokenUtils jwtUtils, MyUserResposistory userResposistory,
            RefreshTokenUtils refreshUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userResposistory = userResposistory;
        this.refreshUtils = refreshUtils;
    }

    public ArrayList<String> authentication(MyUser user) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getMobileNumber(), user.getPassword()));

        } catch (Exception e) {
            throw new Exception("Invalid username or password");
        }

        MyUser userData = userResposistory.findUserByMobileNumber(user.getMobileNumber()).get();
        // userService.getUserByMobileNumber(user.getMobileNumber());
        // Long id = userData.get().getId();
        // String mobileNumber = userData.get().getMobileNumber();
        // UserDetails userDetails = userService
        // .loadUserByUsername(user.getName());
        // System.out.println(user.getId());
        MyClaims claims = new MyClaims(userData.getId(), userData.getMobileNumber());
        String jwtToken = jwtUtils.generateToken(claims);

        String refreshToken = refreshUtils.generateRefreshToken(claims);
        // cookies implementation
        // Instant start = Instant.parse("2017-10-03T10:15:30.00Z");
        // Instant end = Instant.parse("2017-10-03T10:16:30.00Z");
        // ResponseCookie cookie = ResponseCookie.from("Bearer", jwtToken).maxAge(60 *
        // 60 * 24).build();
        // Cookie cookie = new Cookie();
        // cookie
        // cookie.setMaxAge(60 * 60 * 24);

        // response.addHeader("Authorization", cookie.toString());
        // session.setAttribute("Bearer", jwtToken);
        // ResponseCookie cookies = new ResponseCookie.ResponseCookieBuilder().build()
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("Bearer " + jwtToken);
        tokens.add(refreshToken);

        return tokens;
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

        userResposistory.save(user);
        // return jwtUtils.generateToken(new MyClaims(id, user.getMobileNumber()));
        return "SUCCESS";
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
