package com.app.BookingApp.configuration;

import java.time.LocalDate;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.Roles;
import com.app.BookingApp.repository.MyUserRespository;
import com.app.BookingApp.repository.RolesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConfiguration implements CommandLineRunner{

    private MyUserRespository userRespository;
    private RolesRepository rolesRepository;
    private PasswordEncoder passwordEncoder;
    @Value("${Name}")
    String name;
    @Value("${MobileNumber}")
    String mobileNumber;
    @Value("${EmailAddress}")
    String emailAddress;
    @Value("${DateOfBirth}")
    String dateOfBirth;
    @Value("${Password}")
    String password;

    @Autowired
    public UserConfiguration(MyUserRespository userRespository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.userRespository = userRespository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        MyUser adminUser = new MyUser();
        adminUser.setMobileNumber(this.mobileNumber);
        adminUser.setEmailAddress(this.emailAddress);
        adminUser.setName(this.name);
        adminUser.setPassword(passwordEncoder.encode(this.password));
        adminUser.setDateOfBirth(LocalDate.parse(this.dateOfBirth));
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
