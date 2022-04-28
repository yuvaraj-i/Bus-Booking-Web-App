package com.app.BookingApp.controllers;

import javax.servlet.http.Cookie;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.services.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class HomeController {

    private HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody MyUser user) {
        return homeService.authentication(user);

    }

    @PostMapping("/signup")
    public ResponseEntity<Object> createNewUser(@RequestBody MyUser user) {
        return homeService.addNewUser(user);
    }

    // @PostMapping("/auth/renewal")
    // public String verifyAccessToken(@RequestBody String token){
    //     return homeService.renewAccessToken(token);
    // }

}
