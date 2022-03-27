package com.app.BookingApp.user.account;

import org.springframework.stereotype.Component;

@Component
public class UserRegisterationServices {

    private UserRegisterationReposistory userRegisterationReposistory;

    public UserRegisterationServices(UserRegisterationReposistory userRegisterationReposistory) {
        this.userRegisterationReposistory = userRegisterationReposistory;
    }

    public void createAccount(UserRegister userRegister) {
        userRegisterationReposistory.save(userRegister);
    }

    public void createAccount(Long userId, String password) {
    }
    
}
