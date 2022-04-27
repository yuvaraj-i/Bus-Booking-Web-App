package com.app.BookingApp.controllers;

import java.util.ArrayList;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.services.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ArrayList<String> createAuthenticationToken(@RequestBody MyUser user) throws Exception {
        return homeService.authentication(user);

    }

    @PostMapping("/signup")
    public String createNewUser(@RequestBody MyUser user) {
        return homeService.addNewUser(user);
    }

    // @PostMapping("/auth/renewal")
    // public String verifyAccessToken(@RequestBody String token){
    //     return homeService.renewAccessToken(token);
    // }

}
