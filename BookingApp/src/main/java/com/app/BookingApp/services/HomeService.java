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
            // throw new Exception("Invalid username or password");
            return new ResponseEntity<Object>("Invalid password", HttpStatus.UNAUTHORIZED);
             
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
        // Cookie cookie = new Cookie("Bearer", jwtToken);
        // cookie
        // cookie.setMaxAge(60 * 60 * 24);
        // response.addCookie(cookie);

        // response.addHeader("Authorization", cookie.toString());
        // session.setAttribute("Bearer", jwtToken);
        // ResponseCookie cookies = new ResponseCookie.ResponseCookieBuilder().build()
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

}
