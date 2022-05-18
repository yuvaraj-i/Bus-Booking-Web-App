package com.app.BookingApp.services;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.app.BookingApp.configuration.JwtTokenUtils;
import com.app.BookingApp.configuration.RefreshTokenUtils;
import com.app.BookingApp.models.MyClaims;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.Roles;
import com.app.BookingApp.repository.MyUserRespository;
import com.app.BookingApp.repository.RolesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtils jwtUtils;
    private MyUserRespository userResposistory;
    private RefreshTokenUtils refreshUtils;
    private PasswordEncoder passwordEncoder;
    private RolesRepository rolesRepository;

    @Autowired
    public HomeService(AuthenticationManager authenticationManager,
            JwtTokenUtils jwtUtils, MyUserRespository userResposistory,
            RefreshTokenUtils refreshUtils, PasswordEncoder passwordEncoder, RolesRepository rolesRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userResposistory = userResposistory;
        this.refreshUtils = refreshUtils;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }

    public ResponseEntity<Object> authentication(MyUser user, HttpServletResponse response) {

        Optional<MyUser> optionalMobileNumber = userResposistory.findUserByMobileNumber(user.getMobileNumber());

        if (!optionalMobileNumber.isPresent()) {
            return new ResponseEntity<Object>("Invalid mobile number", HttpStatus.UNAUTHORIZED);

        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getMobileNumber(), user.getPassword()));

        }
        catch (DisabledException e) {
            return new ResponseEntity<Object>("Account Locked Contact Admin", HttpStatus.UNAUTHORIZED);

        }
        catch (BadCredentialsException e) {
            return new ResponseEntity<Object>("Invalid Password", HttpStatus.BAD_REQUEST);

        }

        MyUser userData = userResposistory.findUserByMobileNumber(user.getMobileNumber()).get();
        MyClaims claims = new MyClaims(userData.getMobileNumber());
        String jwtToken = jwtUtils.generateToken(claims);
        String refreshToken = refreshUtils.generateRefreshToken(claims);

        Cookie jwtCookie = addCookie("Authorization_1", jwtToken, 60 * 60 * 30);
        response.addCookie(jwtCookie);

        Cookie refreshCookie = addCookie("Authorization_2", refreshToken, 60 * 60 * 30);
        response.addCookie(refreshCookie);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
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

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        MyUser savedUser = userResposistory.save(user);

        Roles userRoles = new Roles();
        userRoles.setUser(savedUser);

        // if(user.getRole() == null) {
        userRoles.setRole("user");
        // }
        // else {
        // userRoles.setRole("user");

        // }

        rolesRepository.save(userRoles);
        // Iterable<Roles> values = rolesRepository.findByUserId(savedUser.getId());
        // Iterator<Roles> iter = values.iterator();
        // while (iter.hasNext()) {
        // Roles role = iter.next();
        // System.out.println(role.getRole());

        // }

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);

    }

    private Cookie addCookie(String cookieName, String cookieValue, int expirationTime) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(expirationTime);

        return cookie;

    }

    public ResponseEntity<Object> signout(HttpServletResponse response) {

        Cookie jwtCookie = addCookie("Authorization_1", null, 0);
        Cookie refreshCookie = addCookie("Authorization_2", null, 0);

        response.addCookie(jwtCookie);
        response.addCookie(refreshCookie);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }

    public ResponseEntity<Object> accessDeniedError() {
        return new ResponseEntity<Object>("Access Denied Contact Admin", HttpStatus.BAD_REQUEST);
    }

}
