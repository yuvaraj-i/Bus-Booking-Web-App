package com.app.BookingApp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class UserController {

  private UserService userService;
  
  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

    @PostMapping(path="add")
    public void registerNewUser(@RequestBody User user) {
      userService.addNewUser(user);
    }

    @GetMapping(path="/all")
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
