package com.app.BookingApp.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @PostMapping(path = "add")
  public void registerNewUser(@RequestBody User user) {
    userService.addNewUser(user);
  }

  @GetMapping(path = "all")
  public Iterable<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping(path = "user/{id}")
  public Optional<User> getUser(@PathVariable("id") Long id) {
    return userService.getUser(id);
  }

  @DeleteMapping(path = "{id}")
  public void deleteExtistingUser(@PathVariable("id") Long userId) {
    userService.deleteExtistingUser(userId);
  }

}
