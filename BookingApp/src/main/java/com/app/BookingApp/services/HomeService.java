package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.Optional;

import com.app.BookingApp.configuration.JwtTokenUtils;
import com.app.BookingApp.configuration.RefreshTokenUtils;
import com.app.BookingApp.models.MyClaims;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.repository.MyUserRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtils jwtUtils;
    private MyUserRespository userResposistory;
    private RefreshTokenUtils refreshUtils;

    @Autowired
    public HomeService(AuthenticationManager authenticationManager,
            JwtTokenUtils jwtUtils, MyUserRespository userResposistory,
            RefreshTokenUtils refreshUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userResposistory = userResposistory;
        this.refreshUtils = refreshUtils;
    }

    public ResponseEntity<Object> authentication(MyUser user) {

        Optional<MyUser> optionalMobileNumber = userResposistory.findUserByMobileNumber(user.getMobileNumber());

        if(!optionalMobileNumber.isPresent()){
            return new ResponseEntity<Object>("Invalid mobile number", HttpStatus.UNAUTHORIZED);

        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getMobileNumber(), user.getPassword()));

        } catch (Exception e) {
            return new ResponseEntity<Object>("Invalid password", HttpStatus.UNAUTHORIZED);
             
        }

        MyUser userData = userResposistory.findUserByMobileNumber(user.getMobileNumber()).get();
        MyClaims claims = new MyClaims(userData.getId(), userData.getMobileNumber());
        String jwtToken = jwtUtils.generateToken(claims);
        String refreshToken = refreshUtils.generateRefreshToken(claims);
        ArrayList<String> tokens = new ArrayList<>();
        
        tokens.add("Bearer " + jwtToken);
        tokens.add(refreshToken);
        
        return new ResponseEntity<Object>(tokens, HttpStatus.OK);

    }

    public ResponseEntity<Object> addNewUser(MyUser user) {
        Optional<MyUser> optionalEmail = userResposistory.findUserByEmailAddress(user.getEmailAddress());
        Optional<MyUser> optionalMobile = userResposistory.findUserByMobileNumber(user.getMobileNumber());

        if (optionalEmail.isPresent()) {
            return new ResponseEntity<Object>("Email Already Present", HttpStatus.CONFLICT);
        }

        if (optionalMobile.isPresent()) {
            return new ResponseEntity<Object>("Mobile Number Already Present", HttpStatus.CONFLICT);
            
        }

        userResposistory.save(user);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);

    }

    public String renewAccessToken(String token) {
        return null;
    }

}
