package com.app.BookingApp.configuration;

import java.time.LocalDate;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.Roles;
import com.app.BookingApp.repository.MyUserRespository;
import com.app.BookingApp.repository.RolesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConfiguration implements CommandLineRunner{

    private MyUserRespository userRespository;
    private RolesRepository rolesRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserConfiguration(MyUserRespository userRespository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.userRespository = userRespository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String name = "Yuvaraj";
        String mobileNumber = "7708852877";
        String emailAddress = "yuvaraj@gmail.com";
        LocalDate dateOfBirth = LocalDate.parse("2000-05-19");
        String password = "yuvaraj19";

        MyUser adminUser = new MyUser();
        adminUser.setMobileNumber(mobileNumber);
        adminUser.setEmailAddress(emailAddress);
        adminUser.setName(name);
        adminUser.setPassword(passwordEncoder.encode(password));
        adminUser.setDateOfBirth(dateOfBirth);
        MyUser saveAdmin = userRespository.save(adminUser);

        Roles userRole = new Roles();
        userRole.setUser(saveAdmin);
        userRole.setRole("user");

        Roles adminRole = new Roles();
        adminRole.setUser(saveAdmin);
        adminRole.setRole("admin");

        rolesRepository.save(userRole);
        rolesRepository.save(adminRole);

    }
    
}
