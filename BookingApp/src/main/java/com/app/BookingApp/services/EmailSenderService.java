package com.app.BookingApp.services;

import java.util.Optional;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.repository.MyUserRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private MyUserRespository userRespository;
    private JavaMailSender emailSender;


    @Autowired
    public EmailSenderService(MyUserRespository userRespository, JavaMailSender emailSender) {
        this.userRespository = userRespository;
        this.emailSender = emailSender;
    }


    public void sendEmailVerficationCode(Long userId) {
        Optional<MyUser> optionalUser = userRespository.findById(userId);

        if(!optionalUser.isPresent()){
            // return failed to found user name response
        }

        String userEmail = optionalUser.get().getEmailAddress();

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(userEmail);
        mailMessage.setSubject("Test by Yuvaraj");
        mailMessage.setText("test mail from java code");
        
        emailSender.send(mailMessage);

        // return "success
    }

    
}
