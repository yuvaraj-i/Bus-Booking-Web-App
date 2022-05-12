package com.app.BookingApp.controllers;

import javax.servlet.http.HttpServletResponse;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.services.HomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class HomeController {
    
    private HomeService homeService;
    
    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody MyUser user, HttpServletResponse response) {
        return homeService.authentication(user, response);

    }

    @PostMapping("/signup")
    public ResponseEntity<Object> createNewUser(@RequestBody MyUser user) {
        return homeService.addNewUser(user);
    }

    @GetMapping("/signout")
    public ResponseEntity<Object> clearResponseCookies(HttpServletResponse response) {
        return homeService.signout(response);
    }

    @GetMapping("/accessDenied")
    public ResponseEntity<Object> accessDeniedError() {
        return homeService.accessDeniedError();
    }


}
