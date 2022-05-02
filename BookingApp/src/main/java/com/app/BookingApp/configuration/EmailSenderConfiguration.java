package com.app.BookingApp.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderConfiguration {
    @Value("${springMailAddress}")
    private String springAppEmailAddress;
    @Value("${springMailPassword}")
    private String SpringAppPassword;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        
        mailSender.setUsername(springAppEmailAddress);
        mailSender.setPassword(SpringAppPassword);
        
        Properties mailProperties = mailSender.getJavaMailProperties();
        mailProperties.put("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.starttls.enable", true);
        mailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        return mailSender;
    } 
    
}
