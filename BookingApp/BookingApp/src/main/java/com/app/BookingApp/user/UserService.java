package com.app.BookingApp.user;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private UserResposistory userResposistory;

    @Autowired
    public UserService(UserResposistory userResposistory) {
        this.userResposistory = userResposistory;
    }

    public Iterable<User> getAllUsers() {
        return userResposistory.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userResposistory.findById(id);
    }

    public void addNewUser(User user) {
        Optional<User> optionalEmail = userResposistory.findUserByEmailAddress(user.getEmailAddress());
        Optional<User> optionalMobile = userResposistory.findUserByMobileNumber(user.getMobileNumber());

        // data checks
        if (!isNameValid(user.getName().trim())) {
            throw new IllegalArgumentException("name formate error");
        }

        if (!isMobileNumberValid(user.getMobileNumber().trim())) {
            throw new IllegalArgumentException("invalid mobile number");
        }

        if (!isEmailValid(user.getEmailAddress().trim())) {
            throw new IllegalArgumentException("invalid Email number");
        }

        if(!isValidPassword(user.getPassword().trim())){
            throw new IllegalArgumentException("invalid Email number");
        }

        if (optionalEmail.isPresent()) {
            throw new IllegalArgumentException("Email Already Present");
        }

        if (optionalMobile.isPresent()) {
            throw new IllegalArgumentException("Mobile Number Already Present");
        }

        userResposistory.save(user);
    }

    public void deleteExtistingUser(Long id) {
        boolean iSUserExist = userResposistory.existsById(id);

        if (!iSUserExist) {
            throw new IllegalStateException("user with " + id + " not found!");
        }

        userResposistory.deleteById(id);
    }

    @Transactional
    public void updateUser(Long id, String name, String emailAddress) {
        Optional<User> user = userResposistory.findById(id);

        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

    }

    public String checkUserLogin(User details) {
        if (details.getMobileNumber() == null || details.getMobileNumber() == null){
            throw new IllegalArgumentException("no input found");
        }
        Optional<User> user = userResposistory.findUserByMobileNumber(details.getMobileNumber());

        if (!user.isPresent()) {
            throw new IllegalArgumentException("User Not found");
        }

        String userPassword = userResposistory.findUserByMobileNumber(details.getMobileNumber()).get().getPassword();

        if (!userPassword.equals(details.getPassword())) {
            throw new IllegalArgumentException("Worng password");
        }

        return "SUCCESS";

    }

    private boolean isEmailValid(String email) {
        if (email == null){
            return false;
        }

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isMobileNumberValid(String number) {
        if (number == null){
            return false;
        }
            
        String regex = "^\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    private boolean isNameValid(String name){
        if (name == null){
            return false;
        }
        // String regex = "\\d";
        // Pattern pattern = Pattern.compile(regex);
        // Matcher matcher = pattern.matcher(name);
        // return matcher.matches();
        return (name.length() > 0) && (name.length() <= 30);
    }

    private boolean isValidPassword(String password){
        if (password == null){
            return false;
        }
        return password.length() > 8;
    }
}
