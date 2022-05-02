package com.app.BookingApp.controllers;

import com.app.BookingApp.services.EmailSenderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class EmailSenderController {
    
    private EmailSenderService emailSenderService;

    @Autowired
    public EmailSenderController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/email/{userId}")
    public void sendEmailForVerification(@PathVariable("userId") Long userId){
        emailSenderService.sendEmailVerficationCode(userId);
    }

}
