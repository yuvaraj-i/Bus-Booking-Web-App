package com.app.BookingApp.controllers;

import java.util.Optional;

import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.services.MyUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyUserController {

  private MyUserService userService;

  @Autowired
  public MyUserController(MyUserService userService) {
    this.userService = userService;

  }

  // @PostMapping(path = "/signup")
  // // @ResponseStatus(code = HttpStatus.OK, reason = "OK")
  // public String registerNewUser(@RequestBody MyUser user) {
  //   return userService.addNewUser(user);
  // }

  @GetMapping(path = "/{id}")
  public Optional<MyUser> getUser(@PathVariable("id") Long id) {
    return userService.getUserById(id);
  }

  @GetMapping(path = "/all")
  public Iterable<MyUser> getAllUsers() {
    return userService.getAllUsers();
  }

  @DeleteMapping(path = "update/{id}")
  public void deleteExtistingUser(@PathVariable("id") Long userId) {
    userService.deleteExtistingUser(userId);
  }

  @PutMapping(path = "update/{userId}")
  public void updateUser(@PathVariable("userId") Long id,
      @RequestParam(required = false) String emailAddress,
      @RequestParam(required = false) String name) {

    userService.updateUser(id, name, emailAddress);

  }

}
