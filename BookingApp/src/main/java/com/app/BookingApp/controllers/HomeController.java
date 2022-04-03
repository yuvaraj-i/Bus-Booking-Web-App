package com.app.BookingApp.controllers;

import javax.servlet.http.HttpServletResponse;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.services.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/login")
    public String createAuthenticationToken(@RequestBody MyUser user, HttpServletResponse response) throws Exception {
        return homeService.authentication(user, response);

    }

    @PostMapping("/signup")
    public String createNewUser(@RequestBody MyUser user) {
        return homeService.addNewUser(user);
    }

}
